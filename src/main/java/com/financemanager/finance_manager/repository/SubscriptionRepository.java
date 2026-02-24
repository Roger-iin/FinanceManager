package com.financemanager.finance_manager.repository;

import com.financemanager.finance_manager.model.SubscriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionModel, UUID> {

   List<SubscriptionModel> findByUserId(UUID userId);

   List<SubscriptionModel> findByUserIdAndActiveTrue(UUID userID);

   Optional<SubscriptionModel> findByIdAndUserID(UUID id, UUID userID);


}
