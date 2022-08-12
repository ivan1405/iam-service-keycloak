package com.chapter.backend.services.iamservice.crud.repository;

import com.chapter.backend.services.iamservice.crud.entity.ActivationTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationTokenRepository extends CrudRepository<ActivationTokenEntity, Integer> {

    Optional<ActivationTokenEntity> findByAccountId(String accountId);
}