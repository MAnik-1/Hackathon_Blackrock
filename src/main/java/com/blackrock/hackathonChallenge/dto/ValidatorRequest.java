package com.blackrock.hackathonChallenge.dto;

import java.util.List;

public class ValidatorRequest {

    private double wage;
    private List<Transaction> transactions;

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
