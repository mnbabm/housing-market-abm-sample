package model;

import lombok.Getter;
import lombok.Setter;
import util.OwnFunctions;

import java.util.ArrayList;
import java.util.*;

@Getter
@Setter
public class Bank implements HasID {

    public static int nextId;
    final int id;

    //state variables
    public double yearlyInterestRateRegressionConstant;
    public double yearlyInterestRateRegressionCoeffLnWageIncome;
    public double yearlyInterestRateRegressionCoeffLTV;
    public double yearlyInterestRateRegressionCoeffAgeCategory1;
    public double yearlyInterestRateRegressionCoeffAgeCategory2;
    public double bridgeLoanToValue;

    List<LoanContract> loanContractsList = new ArrayList<LoanContract>();
    double loanTotal;
    Map<Integer, LoanContract> loanContracts = new HashMap<>();

    //derived variables
    double PTI;
    double LTV;


    public Bank() {
        id = Bank.nextId;
        Bank.nextId++;
    }

    void setBankStrategyParameters() {
        PTI = Model.PTI;
        LTV = Model.LTV;
    }


    double calculatePayment(double loan, double monthlyInterestRate, double duration) {
        double payment = loan * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -duration)));
        return payment;
    }

    LoanContractOffer loanContractOffer(Household household, Flat flat, double loan) {

        if (household.getWageIncome() == 0) return null;
        if (household.hasNonPerformingLoan) return null;
        if (household.lastNonPerformingPeriod > Model.period - Model.nPeriodsInNegativeKHR) return null;
        if (flat.isForcedSale) return null;

        LoanContractOffer loanContractOffer = new LoanContractOffer();

        double totalLoan = 0;
        double totalPayment = 0;

        for (LoanContract loanContract : household.getLoanContracts()) {
            totalLoan += loanContract.principal;
            totalPayment += loanContract.payment;
        }

        double principal = 0;
        double bridgeLoanPrincipal = 0;

        if (Model.phase != Model.Phase.RENOVATEFLATS && household.canGetBridgeLoanInPeriod && household.home != null && flat != household.home && (Model.phase == Model.Phase.FICTIVECHOICEOFHOUSEHOLDS || Model.phase == Model.Phase.HOUSEHOLDPURCHASES)) {
            Flat home = household.home;
            LoanContract homeLoanContract = home.loanContract;

            if (homeLoanContract == null) {
                bridgeLoanPrincipal = Math.min(loan, household.home.getMarketPrice() * bridgeLoanToValue);
            }

            if (homeLoanContract != null && homeLoanContract.bridgeLoanPrincipal == 0 && homeLoanContract.principal < home.getEstimatedMarketPrice() * bridgeLoanToValue) {
                double refinancingLoan = homeLoanContract.principal * (1 + homeLoanContract.monthlyInterestRate);
                loan += refinancingLoan;

                bridgeLoanPrincipal = Math.min(loan, household.home.getEstimatedMarketPrice() * bridgeLoanToValue);
                totalLoan -= refinancingLoan;
                totalPayment -= home.loanContract.payment;
            }

        }
        principal = loan - bridgeLoanPrincipal;
        double bridgeLoanMonthlyInterestRate = calculateBridgeLoanMonthlyInterestRate(bridgeLoanPrincipal, household, flat);
        int bridgeLoanDuration = Model.bridgeLoanDuration;

        int duration = Model.maxDuration;
        double monthlyInterestRate = calculateMonthlyInterestRate();
        double payment = calculatePayment(principal, monthlyInterestRate, duration);


        if (appropriatePTI(household, payment, totalPayment) == false) {
            return null;
        }
        if (Model.phase != Model.Phase.RENOVATEFLATS && principal / (flat.forSalePrice * discountFactorForFlatUnderConstruction(flat)) > LTVForHousehold(household))
            return null;

        loanContractOffer.bridgeLoanPrincipal = bridgeLoanPrincipal;
        loanContractOffer.bridgeLoanMonthlyInterestRate = bridgeLoanMonthlyInterestRate;
        loanContractOffer.bridgeLoanDuration = bridgeLoanDuration;

        loanContractOffer.principal = principal;
        loanContractOffer.monthlyInterestRate = monthlyInterestRate;
        if (Model.phase != Model.Phase.RENOVATEFLATS) {
            loanContractOffer.duration = duration;
        } else {
            loanContractOffer.duration = Model.renovationLoanDuration;
        }
        loanContractOffer.payment = payment;

        loanContractOffer.bank = this;

        loanContractOffer.maxLoan = calculateMaxLoanForLoanContractOffer(household, flat, totalLoan, totalPayment, duration);


        return loanContractOffer;
    }

    double calculateMaxLoanForLoanContractOffer(Household household, Flat flat, double totalLoan, double totalPayment, int duration) {
        double interestRateForMaxLoanCalculation = calculateMonthlyInterestRate();
        double paymentForUnitLoan = calculatePayment(1, interestRateForMaxLoanCalculation, duration);
        double maxPayment = household.householdIncome * calculatePTILimit(household);

        double maxPaymentPrecision = 100;
        double range = maxPayment + 1;
        while (range > maxPaymentPrecision) {
            range /= 2;
            if (appropriatePTI(household, maxPayment, totalPayment)) {
                maxPayment += range;
            } else {
                maxPayment -= range;
            }

        }
        maxPayment -= maxPaymentPrecision;
        double maxLoanWithPTIConstraint = maxPayment / paymentForUnitLoan;
        double maxLoanWithLTVConstraint = flat.getMarketPrice() * LTVForHousehold(household);

        return Math.min(maxLoanWithPTIConstraint, maxLoanWithLTVConstraint);
    }

    double calculateBridgeLoanMonthlyInterestRate(double bridgeLoan, Household household, Flat flat) {
        return Model.yearlyBaseRate / 12;
    }

    double calculateMonthlyInterestRate() {
        return Model.yearlyBaseRate;
    }

    public boolean appropriatePTI(Household household, double payment, double totalPayment) {
        double unemploymentRateToUse = Model.unemploymentRates[household.getTypeOfMemberWihLowerEducationalLevel()];

        return !((payment + totalPayment) / household.householdIncome > calculatePTILimit(household)) && !(household.householdIncome * (1 - unemploymentRateToUse) < household.minConsumptionLevel + payment);
    }

    public double LTVForHousehold(Household household) {
        if (!household.firstBuyer) {
            return LTV;
        } else {
            return LTV + Model.firstBuyerAdditionalLTV;
        }
    }

    public boolean increaseLoanForRenovation(LoanContract loanContract, double loan) {
        Household household = loanContract.debtor;

        double totalLoan = 0;
        double totalPayment = 0;

        for (LoanContract actLoanContract : household.getLoanContracts()) {
            if (actLoanContract == loanContract) continue;
            totalLoan += actLoanContract.principal;
            totalPayment += actLoanContract.payment;
        }

        double increasedLoan = loanContract.principal + loan;
        if (loanContract.issuedInThisPeriod == false)
            increasedLoan += loanContract.principal * loanContract.monthlyInterestRate;

        if (increasedLoan / loanContract.collateral.forSalePrice > LTV) return false;

        double monthlyInterestRate = calculateMonthlyInterestRate();
        double payment = calculatePayment(increasedLoan, monthlyInterestRate, loanContract.duration);
        int duration = loanContract.duration;

        if ((payment + totalPayment) / household.wageIncome > calculatePTILimit(household)) {

            while (duration <= Model.maxDuration) {

                duration += Model.durationIncreaseInIncreaseLoanForRenovation;
                payment = calculatePayment(increasedLoan, monthlyInterestRate, duration);
                if ((payment + totalPayment) / household.wageIncome < calculatePTILimit(household)) break;
            }

            if ((payment + totalPayment) / household.wageIncome > calculatePTILimit(household)) return false;
        }

        household.creditDeposit(increasedLoan - loanContract.principal);

        loanContract.principal = increasedLoan;
        loanContract.renovationPrincipal += loan;
        loanContract.monthlyInterestRate = monthlyInterestRate;
        loanContract.payment = payment;
        loanContract.duration = duration;

        loanContract.issuedInThisPeriod = true;

        return true;

    }


    double calculateMaxLoanToHousehold(Household household) {

        if (household.wageIncome == 0) return 0;
        if (household.hasNonPerformingLoan) return 0;

        double downpayment = Math.max(household.deposit - household.minDeposit, 0);

        if (household.home != null) {
            if (household.home.loanContract != null && household.home.loanContract.isNonPerforming) return 0;
            if (household.home.loanContract == null) {
                downpayment += household.home.getEstimatedMarketPrice() * bridgeLoanToValue;
            } else if (household.home.loanContract.principal < household.home.getEstimatedMarketPrice() * bridgeLoanToValue) {
                downpayment += household.home.getEstimatedMarketPrice() * bridgeLoanToValue - household.home.loanContract.principal;
            }

        }

        double maxLoanLTV = downpayment / (1 - LTVForHousehold(household)) - downpayment;
        double payment = calculatePayment(maxLoanLTV, Model.minYearlyInterestRate / 12, Model.maxDuration);
        double maxLoanPTI = calculatePTILimit(household) / (payment / household.householdIncome) * maxLoanLTV;

        return Math.min(maxLoanLTV, maxLoanPTI);

    }

    double discountFactorForFlatUnderConstruction(Flat flat) {
        return 1 - flat.nPeriodsLeftForConstruction * Model.maxYearlyInterestRate / 12;
    }

    public double calculatePTILimit(Household household) {
        return PTI;
    }


}
