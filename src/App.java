import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.Transaction;
import models.TransactionType;

public class App {
    public static List<Transaction> transactionList = parseCsv("src/transactions.csv");

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Account ID: ");
        String accountId = scanner.nextLine();
        System.out.println("Enter start Date: ");
        String fromDate = scanner.nextLine();
        System.out.println("Enter end Date: ");
        String toDate = scanner.nextLine();
        scanner.close();
        
        System.out.println("\n" + getRelativeBalance(accountId, fromDate, toDate));
        // System.out.println(getRelativeBalance("ACC334455", "20/10/2018 12:00:00", "20/10/2018 19:00:00"));
    }

    public static String getRelativeBalance(String accountId, String fromDateString, String toDateString)
            throws ParseException {
        Date fromDate = stringToDate(fromDateString);
        Date toDate = stringToDate(toDateString);

        // filter transactions related to the input accountID
        Stream<Transaction> transactionStream = transactionList.stream()
                .filter(transaction -> (transaction.getFromAccountId().equals(accountId)
                        || transaction.getToAccountId().equals(accountId)));

        // create a list of reversal transactions for lookup
        List<Transaction> reversalTransactions = transactionList.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.REVERSAL)
                .collect(Collectors.toList());

        // filter transactions occured between the input date constraints
        transactionStream = transactionStream
                .filter(transaction -> (transaction.getDate().after(fromDate) && transaction.getDate().before(toDate)));

        // collect the stream back into a List
        List<Transaction> accountTransactions = transactionStream.collect(Collectors.toList());

        // reduce the list into a relative balance and count transactions
        double balance = 0;
        int transactionCount = 0;
        for (Transaction tx : accountTransactions) {
            if (!hasReversal(tx, reversalTransactions)) {
                transactionCount++;
                if (tx.getFromAccountId().equals(accountId)) {
                    balance -= tx.getAmount();
                } else {
                    balance += tx.getAmount();
                }
            }
        }

        String balanceString = "$";
        if (balance < 0) {
            balanceString = "-" + balanceString + Math.abs(balance);
        } else {
            balanceString = balanceString + Math.abs(balance);
        }

        String output = "";
        output += "Relative balance for the period is: " + balanceString;
        output += "\nNumber of transactions included is: " + transactionCount;

        return output;
    }

    public static List<Transaction> parseCsv(String filePath) {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = "";
            while ((line = reader.readLine()) != null) {
                transactionList.add(parseTransaction(line));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactionList;
    }

    public static Transaction parseTransaction(String line) throws ParseException {
        String[] values = line.split(",");
        String transactionId = values[0].trim();
        String fromAccountId = values[1].trim();
        String toAccountId = values[2].trim();
        Date createdAt = stringToDate(values[3].trim());
        double amount = Double.parseDouble(values[4].trim());
        TransactionType transactionType = TransactionType.valueOf(values[5].trim());
        Transaction transaction;
        if (values.length == 7) {
            String relatedTransaction = values[6].trim();
            transaction = new Transaction(transactionId, fromAccountId, toAccountId, createdAt, amount, transactionType,
                    relatedTransaction);
        } else {
            transaction = new Transaction(transactionId, fromAccountId, toAccountId, createdAt, amount,
                    transactionType);
        }
        return transaction;
    }

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = formatter.parse(dateString);
        return date;
    }

    public static boolean hasReversal(Transaction transaction, List<Transaction> reversals) {
        for (Transaction tx : reversals) {
            if (tx.getRelatedTransaction().equals(transaction.getId())) {
                return true;
            }
        }
        return false;
    }
}
