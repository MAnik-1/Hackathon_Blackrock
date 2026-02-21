package com.blackrock.hackathonChallenge.controller;

import com.blackrock.hackathonChallenge.dto.*;
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

    @PostMapping("/transactions:validator")
    public ValidationResponse validate(
            @RequestBody ValidatorRequest request) {

        return transactionService.validateTransactions(request);
    }

    @PostMapping("/transactions:filter")
    public ValidationResponse filter(
            @RequestBody FilterRequest request) {

        return transactionService.filterTransactions(request);
    }
}
