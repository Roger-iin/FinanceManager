package com.financemanager.finance_manager.repository;

import com.financemanager.finance_manager.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, UUID> {

    List<AccountModel> findByUserId(UUID userId);

    Optional<AccountModel> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);

}
