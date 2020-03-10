package com.github.edsonjnior.bankapi.enums;

import lombok.Getter;
import lombok.Setter;

public enum ErrorMessages {
    RECORD_ALREADY_EXISTS("Record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_FUNDS("Account code %s has no funds"),
    NO_RECORD_FOUND("%s with provided id equals %d is not found"),
    ;

    @Getter
    @Setter
    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
