# Problem
Consider the following credit card fraud detection algorithm

A credit card transaction is comprised of the following elements.

* hashed credit card number
* timestamp - of format 'year-month-dayThour:minute:second'
* amount - of format 'dollars.cents'

Transactions are to be received as a comma separated string of elements.
eg. `10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00`

A credit card will be identified as fraudulent if the sum of amounts for a unique hashed credit card number over a 24 hour sliding window period exceeds the price threshold.

Write a method, which when given a sequence of transactions in chronological order, and a price threshold, returns the hashed credit card numbers that have been identified as fraudulent.

## Assumptions
1. Chronological order of transactions is always maintained by transaction data provider. It is not validated.
2. All timestamps are in the same time zone or they are normalised to UTC or to local time zone before being sent to the method.
3. As mentioned in the exercise description, the set of transaction data that is being sent to the method has a single threshold value for all credit cards.
4. Using `SpringBoot` as there were no specific instructions not to use any external library. 
5. Not using any external library to solve the core problem. All the external libraries are used to make the packaging and running test suites easier.

## Tools and Technologies Used
1. Java 8
2. SpringBoot 2.2.4
3. JUnit 5
4. Maven
5. Eclipse

## Design
* Project is organised in a standard structure with a service layer with 2 services, a data transfer object(dto), a custom exception, and a utilities class. Unit tests appear in the standard(conventional) `Spring` structure for the 2 service classes.

* Service implementation is segregated from its definition. `TransactionAuditServiceImpl` contains the method that audits and finds fraudulent transactions, `getCardsWithFraudulentBehaviour(...)`.

* Another service was implemented to validate the inputs, `TransactionInputValidationServiceImpl`. In case if the inputs are malformed, a custom exception, `TransactionFormatException`, is thrown.

* Configurable properties are defined in `application.properties` file. These properties are read by utilities class `ApplicationPropertyManager` and made available across the application.

* Once the textual inputs are received, they are converted to `POJO`s with using dto `TransactionDTO`.

* To avoid possible mathematical errors in currency calculations, `BigDecimal` was used.

* JavaDoc documentation is available for all `public` methods and most of the `private` methods where comments posed value addition to the code. In line comments were written where ever it was necessary to simplify the cases handled.

## Unit Tests
Both service classes, `TransactionInputValidationServiceImplTest` and `TransactionAuditServiceImplTest`, have unit tests. 
Data driven unit testing was done where it was appropriate.

## How to run Application
### Running in IDE
This is a `SpringBoot` `Maven` project.

Unzip the resource bundle(project bundle) and import to your preferred IDE (with `Maven` project support) as a `Maven` project.

Run `APTouchApplication.java` as a Java application.

A sample simulator method runs to display the functionality for one scenario. All scenarios are found in the unit test class `TransactionAuditServiceImplTest` 

### Running in Terminal
Make sure you have `Maven` locally installed.

Unzip the resource bundle(project bundle) and import to your preferred IDE (with `Maven` project support) as a `Maven` project.

In terminal, go to project directory where the `POM` file is and run `mvn clean install package`

Go to `target` directory in the same project directory and run `java -jar aptouch-0.0.1-SNAPSHOT.jar`

A sample simulator method runs to display the functionality for one scenario. All scenarios are found in the unit test class `TransactionAuditServiceImplTest` 

