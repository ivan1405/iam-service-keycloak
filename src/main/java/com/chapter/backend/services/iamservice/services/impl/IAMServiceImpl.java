package com.chapter.backend.services.iamservice.services.impl;

import com.chapter.backend.services.iamservice.crud.entity.ActivationTokenEntity;
import com.chapter.backend.services.iamservice.crud.repository.ActivationTokenRepository;
import com.chapter.backend.services.iamservice.exception.ServiceException;
import com.chapter.backend.services.iamservice.mapper.AccountMapper;
import com.chapter.backend.services.iamservice.model.AccountDto;
import com.chapter.backend.services.iamservice.services.IAMService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Slf4j
public class IAMServiceImpl implements IAMService {

    private final RealmResource keycloakClient;
    private final AccountMapper accountMapper;
    private final ActivationTokenRepository activationTokenRepository;

    public IAMServiceImpl(final RealmResource keycloakClient,
                          final AccountMapper accountMapper,
                          final ActivationTokenRepository activationTokenRepository) {
        this.keycloakClient = keycloakClient;
        this.accountMapper = accountMapper;
        this.activationTokenRepository = activationTokenRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        UserRepresentation keycloakAccount = accountMapper.toUserRepresentation(accountDto);
        // We will activate the account once the user has confirmed the email
        keycloakAccount.setEmailVerified(false);
        keycloakAccount.setEnabled(false);
        keycloakAccount.setCredentials(Collections.singletonList(createSecureCredentials(accountDto.getPassword())));

        // Create the user in Keycloak
        Response response = keycloakClient.users().create(keycloakAccount);

        if (201 != response.getStatus()) {
            log.error("Error creating the account");
            throw new ServiceException(BAD_REQUEST, "Account not created");
        }

        // Extract the account id from the keycloak response
        String accountId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Generate a new activation token for that account
        String activationToken = UUID.randomUUID().toString();

        accountDto.setActivationToken(activationToken);
        accountDto.setAccountId(accountId);

        ActivationTokenEntity activationTokenEntity = new ActivationTokenEntity();
        activationTokenEntity.setToken(activationToken);
        activationTokenEntity.setAccountId(accountDto.getAccountId());
        activationTokenRepository.save(activationTokenEntity);

        return accountDto;
    }

    @Override
    public void activateAccount(String accountId, String token) {
        UserRepresentation keycloakAccount = getKeycloakAccountById(accountId);
        if (keycloakAccount.isEnabled() && keycloakAccount.isEmailVerified()) {
            throw new ServiceException(BAD_REQUEST, "The account has been already activated");
        }

        // Check that the token belongs to that accountId
        ActivationTokenEntity activationTokenEntity = activationTokenRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ServiceException(NOT_FOUND, "Account was not found"));
        if (!activationTokenEntity.getAccountId().equals(accountId) ||
                !activationTokenEntity.getToken().equals(token)) {
            throw new ServiceException(BAD_REQUEST, "Given params are invalid");
        }

        keycloakAccount.setEnabled(Boolean.TRUE);
        keycloakAccount.setEmailVerified(Boolean.TRUE);
        keycloakClient.users().get(accountId).update(keycloakAccount);

        activationTokenRepository.delete(activationTokenEntity);
    }

    @Override
    public AccountDto getAccountById(String accountId) {
        return accountMapper.toAccountDto(getKeycloakAccountById(accountId));
    }

    @Override
    public void deleteAccount(String accountId) {
        try {
            keycloakClient.users().delete(accountId);
        } catch (NotFoundException notFoundException) {
            throw new ServiceException(NOT_FOUND, "Account was not found");
        }
    }

    @Override
    public void setNewPassword(String accountId, String password) {
        try {
            keycloakClient.users().get(accountId).resetPassword(createSecureCredentials(password));
        } catch (NotFoundException notFoundException) {
            throw new ServiceException(NOT_FOUND, "Account was not found");
        }
    }

    private CredentialRepresentation createSecureCredentials(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(password);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(Boolean.FALSE);
        return credentialRepresentation;
    }

    private UserRepresentation getKeycloakAccountById(String accountId) {
        try {
            return keycloakClient.users().get(accountId).toRepresentation();
        } catch (NotFoundException notFoundException) {
            throw new ServiceException(NOT_FOUND, "Account was not found");
        }
    }
}