import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.Transaction;
import models.TransactionType;


public class App {
    public static void main(String[] args) throws Exception {
        List<Transaction> transactionList = parseCsv();
        System.out.println(transactionList.get(1));
    }


    private static List<Transaction> parseCsv(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/transactions.csv"));
            String line = "";
            while ((line = reader.readLine()) != null) {
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
                    transaction = new Transaction(transactionId, fromAccountId, toAccountId, createdAt, amount,
                            transactionType, relatedTransaction);
                } else {
                    transaction = new Transaction(transactionId, fromAccountId, toAccountId, createdAt, amount,
                            transactionType);
                }
                transactionList.add(transaction);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactionList;
    }

    private static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = formatter.parse(dateString);
        return date;
    }
}
