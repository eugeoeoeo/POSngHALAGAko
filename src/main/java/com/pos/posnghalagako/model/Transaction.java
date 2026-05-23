package com.pos.posnghalagako.model;

import java.time.LocalDateTime;

/**
 * Represents a completed sale / receipt header.
 */
public class Transaction {
    private int id;
    private int cashierId;
    private double totalAmount;
    private double amountPaid;
    private double changeAmount;
    private LocalDateTime transactionDate;

    public Transaction() {
    }

    public Transaction(int cashierId, double totalAmount, double amountPaid, double changeAmount) {
        this.cashierId = cashierId;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.changeAmount = changeAmount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCashierId() { return cashierId; }
    public void setCashierId(int cashierId) { this.cashierId = cashierId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public double getChangeAmount() { return changeAmount; }
    public void setChangeAmount(double changeAmount) { this.changeAmount = changeAmount; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}
