# Conclusion for TICKET-101 and implementation of TICKET-102 

## Table of Contents
- [Overview](#overview)
- [Evaluation](#evaluation)
  - [What was done well](#what-was-done-well)
  - [Areas for improvement](#areas-for-improvement)
  - [The most important shortcoming of TICKET-101](#the-most-important-shortcoming-of-ticket-101)
- [Project before](#project-before)
- [Project after refactoring and implementation of TICKET-102](#project-after-refactoring-and-implementation-of-ticket-102)


## Overview
TICKET-101 aims to implement an MVP of a decision engine for a fintech loan system. The engine processes input such as personal code, requested loan amount, and loan period, to determine and return an optimal loan decision and amount based on predefined rules.

## Evaluation

### What was done well

1. **Functionality**:
    - The engine correctly identifies different customer segments based on their personal code and adjusts the loan offering accordingly.
    - Handles edge cases where the loan amount requested is either lower than what the bank is willing to offer or higher than the maximum possible approved amount.

2. **Code Organization and Structure**:
    - Code is separated into meaningful packages, each corresponding to specific functionality such as exceptions, service logic, and API endpoints which makes navigation and understanding the project straightforward.

3. **Data Validations**:
    - Comprehensive validations ensure that the inputs (personal code, loan amount, loan period) are within acceptable ranges before processing.

4. **Use of Constants**:
    - All key figures and configuration values such as minimum and maximum loan amounts and periods are stored in `DecisionEngineConstants`, making the system configurable and easier to maintain.

### Areas for improvement

1. **SOLID Principles**:
   - `DecisionEngine` handles multiple tasks (validation, loan calculation). Breaking down these responsibilities into more focused classes would be beneficial.

2. **Separation of data model classes to model package**:
   - The classes Decision, DecisionRequest, and DecisionResponse are related to modeling data in application. Organizing these classes into a model package helps in maintaining a clear separation of concerns within the project

### The most important shortcoming of TICKET-101

The main issue was the incomplete credit scoring system **(fixed it)**. 

Additionally, there was a lack of adherence to important principles like SOLID and separation of concerns within the project, which could lead to various issues, described below:

1. **Reduced Maintainability**:
   - Without adherence to SOLID principles, the codebase becomes tightly coupled and harder to maintain. This increases the difficulty of making changes and updates in the future.
   
2. **Decreased Flexibility**: 
   - Lack of adherence to SOLID principles results in code that is less flexible and more brittle. Changes made in one part of the code may have unintended consequences in other parts, leading to a fragile system.
   
3. **Difficulty in Testing**: 
   - Inadequate separation of concerns makes it challenging to isolate and test individual components. This leads to insufficient test coverage and increases the risk of introducing bugs.

4. **Code Duplication**: 
   - Violating SOLID principles often leads to code duplication, as functionality may be scattered across the codebase rather than consolidated in reusable components.

5. **Increased Complexity**: 
   - Projects that do not follow SOLID principles tend to be more complex and harder to understand. This complexity hinders developer productivity and collaboration.

6. **Poor Scalability**: 
   - Lack of adherence to SOLID principles can hinder scalability. As the project grows, it becomes increasingly challenging to add new features or scale the application to handle larger loads.

## Project before

```zsh
.
├── main
│   ├── java
│   │   └── ee
│   │       └── taltech
│   │           └── inbankbackend
│   │               ├── InbankBackendApplication.java
│   │               ├── config
│   │               │   └── DecisionEngineConstants.java
│   │               ├── endpoint
│   │               │   ├── DecisionEngineController.java
│   │               │   ├── DecisionRequest.java
│   │               │   └── DecisionResponse.java
│   │               ├── exceptions
│   │               │   ├── InvalidLoanAmountException.java
│   │               │   ├── InvalidLoanPeriodException.java
│   │               │   ├── InvalidPersonalCodeException.java
│   │               │   └── NoValidLoanException.java
│   │               └── service
│   │                   ├── Decision.java
│   │                   └── DecisionEngine.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── ee
            └── taltech
                └── inbankbackend
                    ├── InbankBackendApplicationTests.java
                    ├── endpoint
                    │   └── DecisionEngineControllerTest.java
                    └── service
                        └── DecisionEngineTest.java
```

## Project after refactoring and implementation of TICKET-102

```zsh
.
├── main
│   ├── java
│   │   └── ee
│   │       └── taltech
│   │           └── inbankbackend
│   │               ├── InbankBackendApplication.java
│   │               ├── config
│   │               │   └── DecisionEngineConstants.java
│   │               ├── endpoint
│   │               │   └── DecisionEngineController.java
│   │               ├── exceptions
│   │               │   ├── InvalidLoanAmountException.java
│   │               │   ├── InvalidLoanPeriodException.java
│   │               │   ├── InvalidPersonalCodeException.java
│   │               │   ├── LowCreditScoreException.java
│   │               │   ├── NoValidLoanException.java
│   │               │   └── NotEligibleAgeException.java
│   │               ├── model
│   │               │   ├── Decision.java
│   │               │   ├── DecisionRequest.java
│   │               │   └── DecisionResponse.java
│   │               ├── service
│   │               │   ├── CreditScoring.java
│   │               │   └── DecisionEngine.java
│   │               └── util
│   │                   ├── AgeValidator.java
│   │                   └── InputValidator.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── ee
            └── taltech
                └── inbankbackend
                    ├── InbankBackendApplicationTests.java
                    ├── endpoint
                    │   └── DecisionEngineControllerTest.java
                    └── service
                        └── DecisionEngineTest.java
```
