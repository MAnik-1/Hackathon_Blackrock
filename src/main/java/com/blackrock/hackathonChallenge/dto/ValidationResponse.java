package com.blackrock.hackathonChallenge.dto;

import java.util.List;

public class ValidationResponse {

    private final List<Transaction> valid;
    private final List<InvalidTransaction> invalid;

    public ValidationResponse(List<Transaction> valid,
                              List<InvalidTransaction> invalid) {
        this.valid = valid;
        this.invalid = invalid;
    }

    public List<Transaction> getValid() {
        return valid;
    }

    public List<InvalidTransaction> getInvalid() {
        return invalid;
    }
}
