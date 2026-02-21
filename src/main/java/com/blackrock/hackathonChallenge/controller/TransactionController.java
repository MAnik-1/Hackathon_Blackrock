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

    @PostMapping("/returns:index")
    public ReturnsResponse calculateIndex(
            @RequestBody ReturnsRequest request) {

        return transactionService.calculateReturns(request, 0.1449, false);
    }

    @PostMapping("/returns:nps")
    public ReturnsResponse calculateNps(
            @RequestBody ReturnsRequest request) {

        return transactionService.calculateReturns(request, 0.0711, true);
    }

    @PostMapping("/performance")
    public PerformanceResponse performance() {

        long start = System.currentTimeMillis();

        Runtime runtime = Runtime.getRuntime();

        long memory =
                (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

        int threads = Thread.activeCount();

        long execution = System.currentTimeMillis() - start;

        return new PerformanceResponse(execution, memory, threads);
    }

}
