package com.blackrock.hackathonChallenge.dto;

public class Transaction {

    private String date;
    private double amount;
    private double ceiling;
    private double remanent;

    public Transaction(String date, double amount, double ceiling, double remanent) {
        this.date = date;
        this.amount = amount;
        this.ceiling = ceiling;
        this.remanent = remanent;
    }

    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public double getCeiling() { return ceiling; }
    public double getRemanent() { return remanent; }

    public void setRemanent(double remanent) { this.remanent = remanent; }
}
