package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanContractOffer {

    Bank bank;

    double principal;
    int duration;
    double monthlyInterestRate;
    double payment;

    double bridgeLoanPrincipal;
    int bridgeLoanDuration;
    double bridgeLoanMonthlyInterestRate;

    double maxLoan;

}
