package com.blackrock.hackathonChallenge.service;

import com.blackrock.hackathonChallenge.dto.Expense;
import com.blackrock.hackathonChallenge.dto.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

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
}
