
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import models.Transaction;
import models.TransactionType;

public class AppTest {
    App app = new App();

    @Test
    public void testParseTransaction() throws ParseException {
        String line = "TX10005, ACC334455, ACC778899, 21/10/2018 09:30:00, 7.25, PAYMENT";
        Transaction tx = App.parseTransaction(line);

        assertEquals(tx.getId(), "TX10005");
        assertEquals(tx.getFromAccountId(), "ACC334455");
        assertEquals(tx.getToAccountId(), "ACC778899");
        assert (tx.getAmount() == 7.25);
        assertEquals(tx.getTransactionType(), TransactionType.PAYMENT);
    }

    @Test
    public void testStringToDate() {
        String dateString = "20/10/2018 19:45:00";
        try {
            Date date = App.stringToDate(dateString);
            assert (date.before(new Date()));
            assert (date.getTime() == (long) 1540025100000L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRelativeBalance() {
        App.transactionList = new ArrayList<Transaction>();
        try {
            App.transactionList.add(new Transaction("TX10001", "ACC334455", "ACC778899",
                    App.stringToDate("20/10/2018 12:47:55"), 25.00, TransactionType.PAYMENT));
            App.transactionList.add(new Transaction("TX10002", "ACC334455", "ACC998877",
                    App.stringToDate("20/10/2018 17:33:43"), 10.50, TransactionType.PAYMENT));
            App.transactionList.add(new Transaction("TX10003", "ACC998877", "ACC778899",
                    App.stringToDate("20/10/2018 18:00:00"), 5.00, TransactionType.PAYMENT));
            App.transactionList.add(new Transaction("TX10004", "ACC334455", "ACC998877",
                    App.stringToDate("20/10/2018 19:45:00"), 10.50, TransactionType.REVERSAL, "TX10002"));
            App.transactionList.add(new Transaction("TX10005", "ACC334455", "ACC778899",
                    App.stringToDate("21/10/2018 09:30:00"), 7.25, TransactionType.PAYMENT));
            String expectedOutput = "Relative balance for the period is: -$25.0\nNumber of transactions included is: 1";
            String output = App.getRelativeBalance("ACC334455", "20/10/2018 12:00:00", "20/10/2018 19:00:00");
            assert (output.equals(expectedOutput));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    public void testHasReversal() {
        App.transactionList = new ArrayList<Transaction>();
        try {
            Transaction txTrue = new Transaction("TX10002", "ACC334455", "ACC998877",
                    App.stringToDate("20/10/2018 17:33:43"), 10.50, TransactionType.PAYMENT);
            Transaction txFalse = new Transaction("TX10001", "ACC334455", "ACC778899",
                    App.stringToDate("20/10/2018 12:47:55"), 25.00, TransactionType.PAYMENT);

            Transaction reversal = new Transaction("TX10004", "ACC334455", "ACC998877",
                    App.stringToDate("20/10/2018 19:45:00"), 10.50, TransactionType.REVERSAL, "TX10002");
            App.transactionList.add(reversal);

            assertTrue(App.hasReversal(txTrue, App.transactionList));
            assertFalse(App.hasReversal(txFalse, App.transactionList));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
