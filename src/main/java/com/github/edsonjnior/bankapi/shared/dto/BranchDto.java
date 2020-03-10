package com.github.edsonjnior.bankapi.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BranchDto implements Serializable {
    private Long id;
    private String name;
    private String code;
    private BankDto bank;
}
