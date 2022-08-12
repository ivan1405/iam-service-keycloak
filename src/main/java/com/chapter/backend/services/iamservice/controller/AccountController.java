package com.chapter.backend.services.iamservice.controller;

import com.chapter.backend.services.iamservice.AccountsApiDelegate;
import com.chapter.backend.services.iamservice.mapper.AccountMapper;
import com.chapter.backend.services.iamservice.model.AccountDto;
import com.chapter.backend.services.iamservice.model.AccountRequest;
import com.chapter.backend.services.iamservice.model.AccountResponse;
import com.chapter.backend.services.iamservice.model.PasswordRequest;
import com.chapter.backend.services.iamservice.services.IAMService;
import com.chapter.backend.services.iamservice.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static com.chapter.backend.services.iamservice.utils.Constants.ACTIVATE_ACCOUNT_MAIL_TEMPLATE;

@RestController
public class AccountController implements AccountsApiDelegate {

    private final IAMService iamService;
    private final MailService mailService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountController(final IAMService iamService,
                             final MailService mailService,
                             final AccountMapper accountMapper) {
        this.iamService = iamService;
        this.mailService = mailService;
        this.accountMapper = accountMapper;
    }

    public ResponseEntity<AccountResponse> createAccount(AccountRequest accountRequest) {
        AccountDto accountDto = iamService.createAccount(accountMapper.toAccountDto(accountRequest));

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("name", accountDto.getFirstName());
        templateParams.put("email", accountDto.getEmail());
        templateParams.put("link", generateActivationLink(accountDto.getAccountId(), accountDto.getActivationToken()));

        mailService.sendEmail("Account activation", accountDto.getEmail(),
                ACTIVATE_ACCOUNT_MAIL_TEMPLATE, templateParams);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(accountDto.getAccountId())
                        .toUri())
                .body(accountMapper.toAccountResponse(accountDto));
    }

    public ResponseEntity<Void> activateAccount(String accountId, String token) {
        iamService.activateAccount(accountId, token);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<AccountResponse> getAccountById(String accountId) {
        return ResponseEntity.ok().body(accountMapper.toAccountResponse(iamService.getAccountById(accountId)));
    }

    public ResponseEntity<Void> deleteAccount(String accountId) {
        iamService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> setNewPassword(String accountId, PasswordRequest passwordRequest) {
        iamService.setNewPassword(accountId, passwordRequest.getPassword());
        return ResponseEntity.noContent().build();
    }

    private String generateActivationLink(String accountId, String activationToken) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}/activate?token={token}")
                .buildAndExpand(accountId, activationToken)
                .toUriString();
    }
}