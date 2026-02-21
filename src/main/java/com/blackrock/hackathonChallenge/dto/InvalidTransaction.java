package com.blackrock.hackathonChallenge.dto;

public class InvalidTransaction extends Transaction {

    private final String message;

    public InvalidTransaction(String date, double amount,
                              double ceiling, double remanent,
                              String message) {
        super(date, amount, ceiling, remanent);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
