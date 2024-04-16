package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.*;
import ee.taltech.inbankbackend.model.Decision;
import ee.taltech.inbankbackend.util.*;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {

    // Used to store the credit modifier calculated from the personal ID code.
    private int creditModifier = 0;

    /**
     * Calculates the maximum loan amount and period for the customer based on their ID code,
     * the requested loan amount and the loan period.
     *
     * @param personalCode ID code of the customer that made the request.
     * @param loanAmount   Requested loan amount
     * @param loanPeriod   Requested loan period in months
     * @return A Decision object containing the approved loan amount and period, and an error message (if any)
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws NotEligibleAgeException      If the age is not eligible for the loan
     * @throws LowCreditScoreException      If the credit score is lower than needed
     * @throws InvalidLoanAmountException   If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException   If the requested loan period is invalid
     * @throws NoValidLoanException         If there is no valid loan found for the given ID code, loan amount and loan period
     */
    public Decision calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidPersonalCodeException, NotEligibleAgeException, LowCreditScoreException, InvalidLoanAmountException,
            InvalidLoanPeriodException, NoValidLoanException {
        // Validate input parameters
        InputValidator.validateInputs(personalCode, loanAmount, loanPeriod);

        // Validate age
        if (!AgeValidator.isAgeEligible(personalCode)) {
            throw new NotEligibleAgeException("Not eligible for a loan due to age!");
        }

        int outputLoanAmount;

        // Calculate the credit modifier based on the provided personal ID code
        creditModifier = getCreditModifier(personalCode);

        // If no valid credit modifier is found, throw an exception
        if (creditModifier == 0) {
            throw new NoValidLoanException("No valid loan found!");
        }

        // Check the credit score
        CreditScoring.checkCreditScore(creditModifier, loanAmount, loanPeriod);

        while (highestValidLoanAmount(loanPeriod) < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            loanPeriod++;
        }

        if (loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            outputLoanAmount = Math.min(DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT, highestValidLoanAmount(loanPeriod));
        } else {
            throw new NoValidLoanException("No valid loan found!");
        }

        // Return a Decision object containing the approved loan amount and period
        return new Decision(outputLoanAmount, loanPeriod, null);
    }

    /**
     * Calculates the largest valid loan for the current credit modifier and loan period.
     *
     * @return Largest valid loan amount
     */
    private int highestValidLoanAmount(int loanPeriod) {
        return creditModifier * loanPeriod;
    }

    /**
     * Calculates the credit modifier of the customer based on the last four digits of their ID code.
     * Debt - 0000...2499
     * Segment 1 - 2500...4999
     * Segment 2 - 5000...7499
     * Segment 3 - 7500...9999
     *
     * @param personalCode ID code of the customer that made the request.
     * @return Segment to which the customer belongs.
     */
    private int getCreditModifier(String personalCode) {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));

        if (segment < 2500) {
            return 0;
        } else if (segment < 5000) {
            return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        } else {
            return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
        }
    }
}
