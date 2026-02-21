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


}
