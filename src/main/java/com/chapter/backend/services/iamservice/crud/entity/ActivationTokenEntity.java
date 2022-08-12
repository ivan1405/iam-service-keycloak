package com.chapter.backend.services.iamservice.crud.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activation_tokens")
@Getter
@Setter
public class ActivationTokenEntity extends BaseEntity {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "token")
    private String token;
}