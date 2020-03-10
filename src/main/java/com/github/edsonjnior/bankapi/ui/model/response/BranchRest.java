package com.github.edsonjnior.bankapi.ui.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BranchRest {
    private Long id;
    private String code;
    private String name;
    private BankRest bank;
}
