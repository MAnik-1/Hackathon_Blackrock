package com.blackrock.hackathonChallenge.service;

import com.blackrock.hackathonChallenge.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransactionService {

    //parsing
    public List<Transaction> parseExpenses(List<Expense> expenses) {

        List<Transaction> transactions = new ArrayList<>();

        for (Expense expense : expenses) {

            double amount = expense.getAmount();
            double ceiling = Math.ceil(amount / 100.0) * 100;
            double remanent = ceiling - amount;

            transactions.add(
                    new Transaction(
                            expense.getTimestamp(),
                            amount,
                            ceiling,
                            remanent
                    )
            );
        }

        return transactions;
    }
    //input validation
    public ValidationResponse validateTransactions(ValidatorRequest request) {

        List<Transaction> transactions = request.getTransactions();

        List<Transaction> validList = new ArrayList<>();
        List<InvalidTransaction> invalidList = new ArrayList<>();

        Set<String> seenDates = new HashSet<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Transaction tx : transactions) {

            String date = tx.getDate();
            double amount = tx.getAmount();

            // 1. Date format validation
            try {
                LocalDateTime.parse(date, formatter);
            } catch (Exception e) {
                invalidList.add(new InvalidTransaction(
                        date,
                        amount,
                        tx.getCeiling(),
                        tx.getRemanent(),
                        "Invalid date format"
                ));
                continue;
            }

            // 2. Negative amount
            if (amount < 0) {
                invalidList.add(new InvalidTransaction(
                        date,
                        amount,
                        tx.getCeiling(),
                        tx.getRemanent(),
                        "Negative amount not allowed"
                ));
                continue;
            }

            // 3. Amount upper limit
            if (amount >= 500000) {
                invalidList.add(new InvalidTransaction(
                        date,
                        amount,
                        tx.getCeiling(),
                        tx.getRemanent(),
                        "Amount exceeds limit"
                ));
                continue;
            }

            // 4. Duplicate timestamp
            if (seenDates.contains(date)) {
                invalidList.add(new InvalidTransaction(
                        date,
                        amount,
                        tx.getCeiling(),
                        tx.getRemanent(),
                        "Duplicate timestamp"
                ));
                continue;
            }

            seenDates.add(date);
            validList.add(tx);
        }

        return new ValidationResponse(validList, invalidList);
    }

    public ValidationResponse filterTransactions(FilterRequest request) {

        List<Transaction> transactions = request.getTransactions();
        List<PeriodQ> qList = request.getQ();
        List<PeriodP> pList = request.getP();

        List<Transaction> validList = new ArrayList<>();
        List<InvalidTransaction> invalidList = new ArrayList<>();

        Set<String> seenDates = new HashSet<>();


        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Transaction tx : transactions) {

            String dateStr = tx.getDate();
            LocalDateTime txDate;

            try {
                txDate = LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                invalidList.add(new InvalidTransaction(
                        tx.getDate(), tx.getAmount(),
                        tx.getCeiling(), tx.getRemanent(),
                        "Invalid date format"
                ));
                continue;
            }

            if (tx.getAmount() < 0) {
                invalidList.add(new InvalidTransaction(
                        tx.getDate(),
                        tx.getAmount(),
                        tx.getCeiling(),
                        tx.getRemanent(),
                        "Negative amounts are not allowed"
                ));
                continue;
            }

            // Duplicate check
            if (seenDates.contains(dateStr)) {
                invalidList.add(new InvalidTransaction(
                        tx.getDate(), tx.getAmount(),
                        tx.getCeiling(), tx.getRemanent(),
                        "Duplicate transaction"
                ));
                continue;
            }

            seenDates.add(dateStr);

            double updatedRemanent = tx.getRemanent();

            //q rule (fixed override)
            PeriodQ selectedQ = null;

            for (PeriodQ q : qList) {
                LocalDateTime start = LocalDateTime.parse(q.getStart(), formatter);
                LocalDateTime end = LocalDateTime.parse(q.getEnd(), formatter);

                if (!txDate.isBefore(start) && !txDate.isAfter(end)) {

                    if (selectedQ == null ||
                            LocalDateTime.parse(q.getStart(), formatter)
                                    .isAfter(LocalDateTime.parse(selectedQ.getStart(), formatter))) {

                        selectedQ = q;
                    }
                }
            }

            if (selectedQ != null) {
                updatedRemanent = selectedQ.getFixed();
            }

            //p rule (extra)
            for (PeriodP p : pList) {
                LocalDateTime start = LocalDateTime.parse(p.getStart(), formatter);
                LocalDateTime end = LocalDateTime.parse(p.getEnd(), formatter);

                if (!txDate.isBefore(start) && !txDate.isAfter(end)) {
                    updatedRemanent += p.getExtra();
                }
            }

            tx.setRemanent(updatedRemanent);

            boolean inKPeriod = false;

            for (PeriodK k : request.getK()) {

                LocalDateTime start = LocalDateTime.parse(k.getStart(), formatter);
                LocalDateTime end = LocalDateTime.parse(k.getEnd(), formatter);

                if (!txDate.isBefore(start) && !txDate.isAfter(end)) {
                    inKPeriod = true;
                    break;
                }
            }

            tx.setInKPeriod(inKPeriod);

            validList.add(tx);
        }

        return new ValidationResponse(validList, invalidList);
    }

    public ReturnsResponse calculateReturns(
            ReturnsRequest request,
            double rate,
            boolean isNps) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int years = request.getAge() < 60 ? 60 - request.getAge() : 5;

        double inflationRate = request.getInflation() / 100.0;

        List<Transaction> parsedTransactions =
                parseExpenses(request.getTransactions());

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setTransactions(parsedTransactions);
        filterRequest.setQ(request.getQ());
        filterRequest.setP(request.getP());
        filterRequest.setK(request.getK());

        ValidationResponse filtered =
                filterTransactions(filterRequest);

        List<Transaction> validTransactions =
                filtered.getValid();

        double totalAmount = 0;
        double totalCeiling = 0;

        for (Transaction tx : validTransactions) {
            totalAmount += tx.getAmount();
            totalCeiling += tx.getCeiling();
        }

        List<SavingsByDate> savings = new ArrayList<>();

        for (PeriodK k : request.getK()) {

            LocalDateTime start =
                    LocalDateTime.parse(k.getStart(), formatter);
            LocalDateTime end =
                    LocalDateTime.parse(k.getEnd(), formatter);

            double invested = 0;

            for (Transaction tx : validTransactions) {

                LocalDateTime txDate =
                        LocalDateTime.parse(tx.getDate(), formatter);

                if (!txDate.isBefore(start) && !txDate.isAfter(end)) {

                    invested += tx.getRemanent();
                }
            }

            // Compound interest
            double compound =
                    invested * Math.pow(1 + rate, years);

            // Inflation adjustment
            double real =
                    compound / Math.pow(1 + inflationRate, years);

            double profit = real - invested;

            double taxBenefit = 0;

            if (isNps) {

                double annualIncome = request.getWage() * 12;

                double deduction =
                        Math.min(invested,
                                Math.min(annualIncome * 0.10, 200000));

                taxBenefit =
                        calculateTax(annualIncome)
                                - calculateTax(annualIncome - deduction);
            }

            savings.add(
                    new SavingsByDate(
                            k.getStart(),
                            k.getEnd(),
                            invested,
                            profit,
                            taxBenefit
                    )
            );
        }

        return new ReturnsResponse(
                totalAmount,
                totalCeiling,
                savings
        );
    }

    // Tax slabs
    private double calculateTax(double income) {

        if (income <= 700000) return 0;

        if (income <= 1000000)
            return (income - 700000) * 0.10;

        double tax = 300000 * 0.10;

        if (income <= 1200000)
            return tax + (income - 1000000) * 0.15;

        tax += 200000 * 0.15;

        if (income <= 1500000)
            return tax + (income - 1200000) * 0.20;

        tax += 300000 * 0.20;

        return tax + (income - 1500000) * 0.30;
    }



}
