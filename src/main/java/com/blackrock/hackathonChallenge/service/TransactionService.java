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

}
