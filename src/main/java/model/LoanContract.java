package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanContract implements HasID {

    private static int nextId;
    int id;

    //state variables

    Household debtor;
    Flat collateral;
    Bank bank;

    public double principal;
    public double renovationPrincipal;
    public int duration;
    public double monthlyInterestRate;
    public double payment;

    double bridgeLoanPrincipal;
    double bridgeLoanPrincipalOriginal;
    int bridgeLoanDuration;
    double bridgeLoanMonthlyInterestRate;
    Flat bridgeLoanCollateral;

    public boolean isNonPerforming = false;
    public int nNonPerformingPeriods;
    public boolean issuedInThisPeriod = false;

    boolean isHousingLoan = true;


    int periodOfIssuance = -1;


    public LoanContract() {
        id = LoanContract.nextId;
        LoanContract.nextId++;
    }


    public void endBridgeLoan() {
        bridgeLoanPrincipal *= 1 + bridgeLoanMonthlyInterestRate;
        if (bridgeLoanPrincipal > debtor.deposit - debtor.minDeposit) {
            double missingDeposit = bridgeLoanPrincipal - (debtor.deposit - debtor.minDeposit);
            principal += missingDeposit;
            restructureLoan();
            debtor.creditDeposit(missingDeposit);

        }

        debtor.chargeDeposit(bridgeLoanPrincipal);
        bridgeLoanPrincipal = 0;
        bridgeLoanMonthlyInterestRate = 0;
        bridgeLoanDuration = 0;
        bridgeLoanCollateral.setLoanContract(null);
        bridgeLoanCollateral = null;

    }

    public void endLoanContract() {

        if (bridgeLoanPrincipal > 0) {
            endBridgeLoan();
        }


        double interestPayment = principal * monthlyInterestRate;
        if (isHousingLoan && collateral.isForcedSale) {
            debtor.chargeDeposit(Math.min(debtor.deposit, principal));

        } else {
            if (debtor.deposit < principal) {
                debtor.deposit = 0;
            } else {
                debtor.chargeDeposit(principal);
            }
        }

        if (debtor.deposit > interestPayment) {
            debtor.chargeDeposit(interestPayment);
        } else {
            debtor.deposit = 0;
        }


        if (isHousingLoan) {
            collateral.setLoanContract(null);
            collateral.setForcedSale(false);
            if (collateral.isForSale == false) collateral.nForSalePeriods = 0;
        }

        bank.getLoanContracts().remove(id);
        debtor.getLoanContracts().remove(this);
        bank.loanTotal -= principal;

        if (debtor.deposit < 0) debtor.deposit = 0;

        deleteLoanContract();


    }

    public void deleteLoanContract() {
        Model.loanContracts.remove(id);
        Model.loanContractsForParallelComputing.get(id % Model.nThreads).remove(id);
    }

    public void monthlyPayment() {

        if (bridgeLoanPrincipal > 0) bridgeLoanPrincipal *= 1 + bridgeLoanMonthlyInterestRate;
        if (bridgeLoanDuration >= 1) {
            bridgeLoanDuration--;
            if (bridgeLoanDuration == 0) restructureLoanContractWithBridgeLoanPrincipal();
        }

        if (debtor.deposit >= payment && isNonPerforming == false) {
            normalPayment();

        } else {
            isNonPerforming = true;
            nonPerformingPayment();
        }


    }

    private void normalPayment() {
        debtor.chargeDeposit(payment);
        double decreaseInPrincipal = payment - principal * monthlyInterestRate;
        if (renovationPrincipal > 0) renovationPrincipal -= renovationPrincipal / principal * decreaseInPrincipal;
        principal -= decreaseInPrincipal;

        bank.loanTotal -= decreaseInPrincipal;
        duration--;

        if (duration == 0) payment = 0;
        if (duration == 0 && bridgeLoanDuration == 0) endLoanContract();

    }

    private void nonPerformingPayment() {

        if (debtor.deposit > payment && collateral != null && collateral.isForcedSale == false) {
            isNonPerforming = false;
            nNonPerformingPeriods = 0;
            if (isHousingLoan) {
                collateral.setForcedSale(false);
                if (collateral.isForSale == false) collateral.nForSalePeriods = 0;
            }

            normalPayment();

        } else {

            debtor.lastNonPerformingPeriod = Model.period;


            double toPay = Math.min(debtor.deposit, payment);
            double interestPayment = principal * monthlyInterestRate;
            double decreaseInPrincipal = toPay - interestPayment;
            if (renovationPrincipal > 0) renovationPrincipal -= renovationPrincipal / principal * decreaseInPrincipal;
            principal -= decreaseInPrincipal;

            bank.loanTotal -= decreaseInPrincipal;
            debtor.chargeDeposit(toPay);

            duration--;
            payment = bank.calculatePayment(principal, monthlyInterestRate, duration);

            if (principal <= 0) {
                duration = 0;
                isNonPerforming = false;
                nNonPerformingPeriods = 0;
                if (isHousingLoan) {
                    collateral.setForcedSale(false);
                    if (collateral.isForSale == false) collateral.nForSalePeriods = 0;
                }


                if (bridgeLoanPrincipal <= 0) {
                    endLoanContract();
                }
                return;
            }

            if (duration == 0)
                duration = 1; //if somehow there is still principal to pay, it could be paid in the next month
            payment = bank.calculatePayment(principal, monthlyInterestRate, duration);

            nNonPerformingPeriods++;

            if (nNonPerformingPeriods == Model.nNonPerformingPeriodsForRestructuring && nNonPerformingPeriods < Model.nNonPerformingPeriodsForForcedSale && debtor.getWageIncome() > 0) {
                restructureLoan();
            }

            if (nNonPerformingPeriods == Model.nNonPerformingPeriodsForForcedSale) {
                if (isHousingLoan) {
                    collateral.setForcedSale(true);
                    if (collateral.nPeriodsLeftForRent > 1) collateral.setNPeriodsLeftForRent(1);

                }

            }

        }

    }

    void restructureLoanContractWithBridgeLoanPrincipal() {
        principal += bridgeLoanPrincipal;
        bridgeLoanPrincipal = 0;
        endBridgeLoan();
        restructureLoan();
    }

    void restructureLoan() {

        boolean PTIbinding = true;
        if (duration == 0) duration = Model.minDurationForRestructuring;

        while (PTIbinding) {

            double totalLoan = 0;
            double totalPayment = 0;

            for (LoanContract loanContract : debtor.getLoanContracts()) {
                if (loanContract != this) {
                    totalLoan += loanContract.principal;
                    totalPayment += loanContract.payment;
                }
            }

            monthlyInterestRate = bank.calculateMonthlyInterestRate();
            payment = bank.calculatePayment(principal, monthlyInterestRate, duration);
            if ((payment + totalPayment) / debtor.potentialWageIncome <= bank.calculatePTILimit(debtor)) {
                PTIbinding = false;
            } else {
                duration += Model.durationIncreaseInRestructuring;
                if (duration >= Model.maxDuration) {
                    duration = Model.maxDuration;
                    payment = bank.calculatePayment(principal, monthlyInterestRate, duration);
                    return;
                }
            }
        }

    }

    public static synchronized void changeDebtor(LoanContract loanContract, Household household) {
        loanContract.debtor.loanContracts.remove(loanContract);
        loanContract.debtor = household;
        household.loanContracts.add(loanContract);
    }

}
