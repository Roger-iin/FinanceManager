package com.financemanager.finance_manager.dto.transaction;

import com.financemanager.finance_manager.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        String description,
        BigDecimal amount,
        TransactionType type,
        LocalDate date,
        String notes,
        LocalDateTime createdAt,
        UUID accountId,
        String accountName,
        UUID categoryId,
        String categoryName,
        String categoryIcon,
        String categoryColor
) {}
