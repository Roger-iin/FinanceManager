package com.financemanager.finance_manager.dto.subscription;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String name,
        BigDecimal amount,
        Integer billingDay,
        Boolean active,
        LocalDate startDate,
        String notes,
        LocalDateTime createdAt,
        UUID accountId,
        String accountName,
        UUID categoryId,
        String categoryName,
        String categoryIcon,
        String categoryColor
) {}
