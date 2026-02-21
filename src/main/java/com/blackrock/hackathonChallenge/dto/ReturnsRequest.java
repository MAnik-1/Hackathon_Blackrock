package com.blackrock.hackathonChallenge.dto;

import java.util.List;

public class ReturnsRequest {

    private int age;
    private double wage;
    private double inflation;

    private List<PeriodQ> q;
    private List<PeriodP> p;
    private List<PeriodK> k;

    private List<Expense> transactions;

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWage() { return wage; }
    public void setWage(double wage) { this.wage = wage; }

    public double getInflation() { return inflation; }
    public void setInflation(double inflation) { this.inflation = inflation; }

    public List<PeriodQ> getQ() { return q; }
    public void setQ(List<PeriodQ> q) { this.q = q; }

    public List<PeriodP> getP() { return p; }
    public void setP(List<PeriodP> p) { this.p = p; }

    public List<PeriodK> getK() { return k; }
    public void setK(List<PeriodK> k) { this.k = k; }

    public List<Expense> getTransactions() { return transactions; }
    public void setTransactions(List<Expense> transactions) {
        this.transactions = transactions;
    }
}
