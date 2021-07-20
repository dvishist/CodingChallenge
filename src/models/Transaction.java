package models;

import java.util.Date;

public class Transaction {

    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private Date createdAt;
    private double amount;
    private TransactionType transactionType;
    private String relatedTransaction;

    public Transaction(String transactionId, String fromAccountId, String toAccountId, Date createdAt, double amount,
            TransactionType transactionType) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.createdAt = createdAt;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(String transactionId, String fromAccountId, String toAccountId, Date createdAt, double amount,
            TransactionType transactionType, String relatedTransaction) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.createdAt = createdAt;
        this.amount = amount;
        this.transactionType = transactionType;
        this.relatedTransaction = relatedTransaction;
    }

    public String getId() {
        return this.transactionId;
    }

    public String getFromAccountId() {
        return this.fromAccountId;
    }

    public String getToAccountId() {
        return this.toAccountId;
    }

    public Date getDate() {
        return this.createdAt;
    }

    public double getAmount() {
        return this.amount;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public String getRelatedTransaction() {
        return this.relatedTransaction;
    }

    @Override
    public String toString() {
        String output = "";
        output += "-----------------------------------------------";
        output += "\nTransaction ID: " + this.transactionId;
        output += "\nFrom Account: " + this.fromAccountId;
        output += "\nTo Account: " + this.toAccountId;
        output += "\nDate and Time: " + this.createdAt;
        output += "\nAmount: " + this.amount;
        output += "\nTransaction Type: " + this.transactionType;

        if (this.transactionType == TransactionType.REVERSAL) {
            output += "\nReversal for Transaction: : " + this.relatedTransaction;
        }
        output += "\n-----------------------------------------------";
        return output;
    }

}
