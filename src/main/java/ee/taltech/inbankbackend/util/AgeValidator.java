package ee.taltech.inbankbackend.util;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;

import java.time.LocalDate;

/**
 * AgeValidator is a utility class for determining whether a person is eligible for a loan based on their age.
 */
public class AgeValidator {

    /**
     * Determines if a person is eligible for a loan based on their age.
     *
     * @param personalCode the personal code of the person
     * @return true if the person's age is within the acceptable range for a loan, false otherwise
     */
    public static boolean isAgeEligible(String personalCode) {
        int age = determineAgeFromCode(personalCode);
        return (age >= DecisionEngineConstants.MINIMUM_AGE_FOR_LOAN && age <= DecisionEngineConstants.MAXIMUM_AGE_FOR_LOAN);
    }

    /**
     * Determines the age of a person based on their personal code.
     *
     * @param personalCode the personal code of the person
     * @return the age of the person
     */
    private static int determineAgeFromCode(String personalCode) {
        // Extract birth year, month, and day from the personal code
        int birthYear = extractBirthYear(personalCode);
        int birthMonth = extractBirthMonth(personalCode);
        int birthDay = extractBirthDay(personalCode);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate age
        int age = currentDate.getYear() - birthYear;

        // Adjust age if birth month and day have not occurred yet this year
        if (birthMonth > currentDate.getMonthValue() ||
                (birthMonth == currentDate.getMonthValue() && birthDay > currentDate.getDayOfMonth())) {
            age--;
        }

        return age;
    }

    /**
     * Extracts the birth year from the personal code.
     *
     * @param personalCode the personal code of the person
     * @return the birth year of the person
     */
    private static int extractBirthYear(String personalCode) {
        int century = Integer.parseInt(personalCode.substring(0, 1)); // First number
        int yearDigits = Integer.parseInt(personalCode.substring(1, 3)); // Second and third number

        int birthYear;
        if (century == 1 || century == 2) {
            birthYear = 1800 + yearDigits;
        } else if (century == 3 || century == 4) {
            birthYear = 1900 + yearDigits;
        } else if (century == 5 || century == 6) {
            birthYear = 2000 + yearDigits;
        } else {
            birthYear = -1;
        }

        return birthYear;
    }

    /**
     * Extracts the birth month from the personal code.
     *
     * @param personalCode the personal code of the person
     * @return the birth month of the person
     */
    private static int extractBirthMonth(String personalCode) {
        return Integer.parseInt(personalCode.substring(3, 5)); // Fourth and fifth number
    }

    /**
     * Extracts the birth day from the personal code.
     *
     * @param personalCode the personal code of the person
     * @return the birth day of the person
     */
    private static int extractBirthDay(String personalCode) {
        return Integer.parseInt(personalCode.substring(5, 7)); // Sixth and seventh number
    }
}
