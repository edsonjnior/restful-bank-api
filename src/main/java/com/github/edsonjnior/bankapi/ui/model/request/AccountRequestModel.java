package com.github.edsonjnior.bankapi.ui.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountRequestModel {
    private String name;
    private String login;
    private String password;
}
