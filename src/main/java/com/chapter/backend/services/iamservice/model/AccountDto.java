package com.chapter.backend.services.iamservice.model;

import lombok.Data;

@Data
public class AccountDto {

    private String accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String activationToken;
}