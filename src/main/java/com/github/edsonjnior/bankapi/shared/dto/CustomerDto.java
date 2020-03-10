package com.github.edsonjnior.bankapi.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomerDto implements Serializable {
    private Long id;
    private String name;
}
