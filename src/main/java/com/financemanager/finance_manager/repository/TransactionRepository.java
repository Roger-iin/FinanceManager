package com.financemanager.finance_manager.repository;

import com.financemanager.finance_manager.model.TransactionModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository {

    Optional<TransactionModel> findByIdAndUserId(UUID id, UUID userId);

    List<TransactionModel> findByUserIdAndDateBetweenOrderByDateDesc(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<TransactionModel> findByUserIdAndTypeAndDateBetweenOrderBrDateDesc(
            UUID userId,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );

    List<TransactionModel> findByUserIdAndAccountIdAndDateBetweenOrderByDateDesc(
            UUID userId,
            UUID accountId,
            LocalDate startDate,
            LocalDate endDat
    );

    @Query("""
            SELECT SUM(t.amount) FROM transaction t
            WHERE t.user.id = :userId
            AND t.type = :type
            AND t.date BETWEEN :start AND :end
            """)
    BigDecimal sumByUserIdAndTypeAndDateBetween(
            @Param("userID") UUID userID,
            @Param("type") TransactionType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

}
