package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.model.Decision;
import ee.taltech.inbankbackend.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private String debtorPersonalCode;
    private String segment1PersonalCode;
    private String segment2PersonalCode;
    private String segment3PersonalCode;

    private String underAgePersonalCode;
    private String overAgePersonalCode;


    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "35006069515";

        underAgePersonalCode = "61107121760";
        overAgePersonalCode = "30001010004";
    }

    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 4000L, 12));
    }

    /**
     * Tests passes but without credit scoring for that period and amount,
     * with current credit scoring algorithm credit score for that account is 0.30  which is < 1
     * if we want that test to pass with given amount and time with expected results we should accept credit score from 0.3
     **/
//    @Test
//    void testSegment1PersonalCode() throws InvalidLoanPeriodException, NotEligibleAgeException, NoValidLoanException,
//            InvalidPersonalCodeException, InvalidLoanAmountException, LowCreditScoreException {
//        Decision decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 12);
//        assertEquals(2000, decision.getLoanAmount());
//        assertEquals(20, decision.getLoanPeriod());
//    }

    @Test
    void testSegment1PersonalCode() throws InvalidLoanPeriodException, NotEligibleAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, LowCreditScoreException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 48);
        assertEquals(4800, decision.getLoanAmount());
        assertEquals(48, decision.getLoanPeriod());
    }

    /**
     * Tests passes but without credit scoring for that period and amount,
     * with current credit scoring algorithm credit score for that account is 0.89  which is < 1
     * if we want that test to pass with given amount and time with expected results we should accept credit score from 0.5
     **/
//    @Test
//    void testSegment2PersonalCode() throws InvalidLoanPeriodException, NotEligibleAgeException, NoValidLoanException,
//            InvalidPersonalCodeException, InvalidLoanAmountException, LowCreditScoreException {
//        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 4000L, 12);
//        assertEquals(3600, decision.getLoanAmount());
//        assertEquals(12, decision.getLoanPeriod());
//    }

    @Test
    void testSegment2PersonalCode() throws InvalidLoanPeriodException, NotEligibleAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, LowCreditScoreException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 4000L, 24);
        assertEquals(7200, decision.getLoanAmount());
        assertEquals(24, decision.getLoanPeriod());
    }

    @Test
    void testSegment3PersonalCode() throws InvalidLoanPeriodException, NotEligibleAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, LowCreditScoreException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 4000L, 12);
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(invalidPersonalCode, 4000L, 12));
    }

    void testCreditSore() {
        assertThrows(LowCreditScoreException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 12));
    }

    @Test
    void testUnderAge() {
        assertThrows(NotEligibleAgeException.class,
                () -> decisionEngine.calculateApprovedLoan(underAgePersonalCode, 4000L, 12));
    }

    @Test
    void testOverAge() {
        assertThrows(NotEligibleAgeException.class,
                () -> decisionEngine.calculateApprovedLoan(overAgePersonalCode, 4000L, 12));
    }

    @Test
    void testLowCreditScore() {
        assertThrows(LowCreditScoreException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 12));
    }

    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooLowLoanAmount, 12));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooHighLoanAmount, 12));
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooShortLoanPeriod));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooLongLoanPeriod));
    }

    @Test
    void testFindSuitableLoanPeriod() throws InvalidLoanPeriodException, NotEligibleAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, LowCreditScoreException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 2000L, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 10000L, 60));
    }

}

