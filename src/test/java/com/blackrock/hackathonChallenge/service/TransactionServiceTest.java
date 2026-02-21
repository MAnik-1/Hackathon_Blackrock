package com.blackrock.hackathonChallenge.service;

import com.blackrock.hackathonChallenge.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    private TransactionService service;

    @BeforeEach
    void setup() {
        service = new TransactionService();
    }

    @Test
    void shouldCalculateCeilingAndRemanentCorrectly() {

        Expense expense = new Expense();
        expense.setTimestamp("2023-01-01 10:00:00");
        expense.setAmount(250);

        List<Transaction> result =
                service.parseExpenses(List.of(expense));

        assertEquals(300, result.get(0).getCeiling());
        assertEquals(50, result.get(0).getRemanent());
    }

    @Test
    void shouldOverrideRemanentWithQFixedAmount() {

        Transaction tx = new Transaction("2023-07-15 10:00:00",620,700,80);


        PeriodQ q = new PeriodQ(0,"2023-07-01 00:00:00","2023-07-31 23:59:59");


        FilterRequest request = new FilterRequest();
        request.setTransactions(List.of(tx));
        request.setQ(List.of(q));
        request.setP(List.of());
        request.setK(List.of());

        ValidationResponse response =
                service.filterTransactions(request);

        assertEquals(0,
                response.getValid().get(0).getRemanent());
    }

    @Test
    void shouldMarkNegativeAmountAsInvalid() {

        Transaction tx = new Transaction("2023-01-01 10:00:00",-100,100,0);

        ValidatorRequest request = new ValidatorRequest();
        request.setWage(50000);
        request.setTransactions(List.of(tx));

        ValidationResponse response =
                service.validateTransactions(request);

        assertEquals(1, response.getInvalid().size());
    }


    @Test
    void shouldCalculateIndexReturns() {

        ReturnsRequest request = new ReturnsRequest();
        request.setAge(29);
        request.setWage(50000);
        request.setInflation(5.5);

        Expense expense = new Expense();
        expense.setTimestamp("2023-01-01 10:00:00");
        expense.setAmount(250);

        request.setTransactions(List.of(expense));
        request.setQ(List.of());
        request.setP(List.of());

        PeriodK k = new PeriodK();
        k.setStart("2023-01-01 00:00:00");
        k.setEnd("2023-12-31 23:59:59");

        request.setK(List.of(k));

        ReturnsResponse response =
                service.calculateReturns(
                        request,
                        0.1449,   // index rate
                        false     // not NPS
                );

        assertTrue(response.getSavingsByDates().get(0).getAmount() > 0);
        assertTrue(response.getSavingsByDates().get(0).getProfits() > 0);
    }
}