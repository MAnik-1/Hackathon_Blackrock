package com.blackrock.hackathonChallenge.controller;

import com.blackrock.hackathonChallenge.dto.Expense;
import com.blackrock.hackathonChallenge.dto.Transaction;
import com.blackrock.hackathonChallenge.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blackrock/challenge/v1")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions:parse")
    public List<Transaction> parseTransactions(@RequestBody List<Expense> expenses) {
        return transactionService.parseExpenses(expenses);
    }
}
