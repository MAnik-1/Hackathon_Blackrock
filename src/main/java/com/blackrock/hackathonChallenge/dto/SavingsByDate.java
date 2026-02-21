package com.blackrock.hackathonChallenge.dto;

public class SavingsByDate {

    private String start;
    private String end;
    private double amount;
    private double profits;
    private double taxBenefit;

    public SavingsByDate(String start, String end,
                         double amount,
                         double profits,
                         double taxBenefit) {
        this.start = start;
        this.end = end;
        this.amount = amount;
        this.profits = profits;
        this.taxBenefit = taxBenefit;
    }

    public String getStart() { return start; }
    public String getEnd() { return end; }
    public double getAmount() { return amount; }
    public double getProfits() { return profits; }
    public double getTaxBenefit() { return taxBenefit; }
}
