package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.exceptions.LowCreditScoreException;

public class CreditScoring {
    /**
     * Checks if the calculated credit score is below 1, indicating a low credit score.
     * If the credit score is below 1, throws a LowCreditScoreException.
     *
     * @param creditModifier The credit modifier, determined by the last four digits of the customer's ID code.
     * @param loanAmount     The requested loan amount.
     * @param loanPeriod     The requested loan period in months.
     * @throws LowCreditScoreException If the calculated credit score is below 1.
     */
    public static void checkCreditScore(int creditModifier, long loanAmount, int loanPeriod) throws LowCreditScoreException {
        double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriod);
        if (creditScore < 1) {
            throw new LowCreditScoreException("Loan application is denied due to low credit score.");
        }
    }

    /**
     * Calculates the credit score for a loan based on the credit modifier, loan amount, and loan period.
     *
     * @param creditModifier The credit modifier, determined by the last four digits of the customer's ID code.
     * @param loanAmount     The requested loan amount.
     * @param loanPeriod     The requested loan period in months.
     * @return The credit score for the loan, calculated as (creditModifier / loanAmount) * loanPeriod.
     */
    public static double calculateCreditScore(int creditModifier, long loanAmount, int loanPeriod) {
        return ((double) creditModifier / loanAmount) * loanPeriod;
    }
}
