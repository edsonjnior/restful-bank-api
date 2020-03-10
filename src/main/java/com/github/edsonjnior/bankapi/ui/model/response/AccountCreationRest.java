package com.github.edsonjnior.bankapi.ui.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreationRest {
    private Long id;
    private String name;
    private String code;
    private BranchRest branch;
}
