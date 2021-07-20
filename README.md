# CodingChallenge
Coding Challenge for Me bank


## Design
- Transaction class has the relative properties given in the specification.
- TransactionType is an enum with two types PAYMENT and REVERSAL to denote the Transaction Type
- The main App.java file utilises various methods to parse the csv, map the strings to Transaction instances, 
  filter the transactions and generate a relative balance based on input
  
### Utility functions
- parseTransaction: takes a string input which is one row from the input csv and converts it into a Transaction object.
- parseCsv: takes a string which is the file path and maps each line to a Transaction instance and returns an ArrayList of Transaction objects
- stringToDate: takes a date-time string in the format (dd:mm:yyyy hh:mm:ss) as input and converts it into a Date object
- hasReversal: takes two inputs, a Transaction object and a List of reversal transactions, and checks whether the transaction has a respective Reversal transaction for it.
- getRelativeBalance: takes 3 inputs, accountId, start Date and End Date and generates a relative balance for the time period using the above functions.

###Installation
- the repo can be directly cloned and run in any IDE (NetBeans, Eclipse)
- Since I'm using VSCode, there is a JUnit dependency for Vscode in the lib folder.
- You need to install the "coding pack for java extension for VSCode" or any code runner to run the project in VSCode.
