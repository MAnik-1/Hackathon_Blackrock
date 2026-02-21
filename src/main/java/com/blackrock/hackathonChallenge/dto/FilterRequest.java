package com.blackrock.hackathonChallenge.dto;

import java.util.List;

public class FilterRequest {

    private double wage;
    private List<PeriodQ> q;
    private List<PeriodP> p;
    private List<PeriodK> k;
    private List<Transaction> transactions;

    public double getWage() { return wage; }
    public void setWage(double wage) { this.wage = wage; }

    public List<PeriodQ> getQ() { return q; }
    public void setQ(List<PeriodQ> q) { this.q = q; }

    public List<PeriodP> getP() { return p; }
    public void setP(List<PeriodP> p) { this.p = p; }

    public List<PeriodK> getK() { return k; }
    public void setK(List<PeriodK> k) { this.k = k; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
