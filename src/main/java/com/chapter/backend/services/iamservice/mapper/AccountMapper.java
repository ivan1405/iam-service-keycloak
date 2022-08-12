package com.chapter.backend.services.iamservice.mapper;

import com.chapter.backend.services.iamservice.model.AccountDto;
import com.chapter.backend.services.iamservice.model.AccountRequest;
import com.chapter.backend.services.iamservice.model.AccountResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AccountMapper {

    @Mapping(source = "name", target = "firstName")
    AccountDto toAccountDto(AccountRequest accountRequest);

    @Mapping(source = "id", target = "accountId")
    AccountDto toAccountDto(UserRepresentation userRepresentation);

    @Mappings({
            @Mapping(source = "firstName", target = "name"),
            @Mapping(source = "accountId", target = "id")
    })
    AccountResponse toAccountResponse(AccountDto accountDto);

    @Mapping(source = "email", target = "username")
    UserRepresentation toUserRepresentation(AccountDto accountDto);
}