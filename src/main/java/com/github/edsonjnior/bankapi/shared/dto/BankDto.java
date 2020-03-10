package com.github.edsonjnior.bankapi.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BankDto implements Serializable {
    private Long id;
    private String name;
    private String code;

    public BankDto(){}

    public BankDto(Long id){
        this.id = id;
    }
}
