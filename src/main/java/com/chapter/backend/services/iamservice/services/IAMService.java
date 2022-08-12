package com.chapter.backend.services.iamservice.services;

import com.chapter.backend.services.iamservice.model.AccountDto;

public interface IAMService {

    AccountDto createAccount(AccountDto accountDto);

    void activateAccount(String accountId, String token);

    AccountDto getAccountById(String accountId);

    void deleteAccount(String accountId);

    void setNewPassword(String accountId, String password);
}