package com.altis.library.loans.models.enums;

import lombok.Getter;

@Getter
public enum LoansStatus {
    RENTED("Alugado"),
    OVERDUE("Atrasado"),
    RETURNED_ON_TIME("Entregue no prazo"),
    RETURNED_WITH_DELAY("Entregue com atraso");

    private final String description;

    LoansStatus(String description){
        this.description = description;
    }

}
