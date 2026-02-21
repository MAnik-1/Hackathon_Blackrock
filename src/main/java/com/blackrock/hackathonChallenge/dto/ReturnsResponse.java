package com.blackrock.hackathonChallenge.dto;

import java.util.List;

public class ReturnsResponse {

    private double transactionsTotalAmount;
    private double transactionsTotalCeiling;
    private List<SavingsByDate> savingsByDates;

    public ReturnsResponse(double transactionsTotalAmount,
                           double transactionsTotalCeiling,
                           List<SavingsByDate> savingsByDates) {
        this.transactionsTotalAmount = transactionsTotalAmount;
        this.transactionsTotalCeiling = transactionsTotalCeiling;
        this.savingsByDates = savingsByDates;
    }

    public double getTransactionsTotalAmount() {
        return transactionsTotalAmount;
    }

    public double getTransactionsTotalCeiling() {
        return transactionsTotalCeiling;
    }

    public List<SavingsByDate> getSavingsByDates() {
        return savingsByDates;
    }
}
