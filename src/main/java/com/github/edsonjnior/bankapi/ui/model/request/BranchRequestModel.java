package com.github.edsonjnior.bankapi.ui.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BranchRequestModel {
    private String name;
    private String code;
    private Long bank;
}
