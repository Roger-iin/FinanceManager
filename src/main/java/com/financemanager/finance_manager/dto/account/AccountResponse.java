package com.financemanager.finance_manager.dto.account;

import com.financemanager.finance_manager.model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name,
        AccountType type,
        String color,
        BigDecimal balance,
        LocalDateTime createdAt
) {}
