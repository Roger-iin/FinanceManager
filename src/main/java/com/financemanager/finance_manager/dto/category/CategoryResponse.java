package com.financemanager.finance_manager.dto.category;

import com.financemanager.finance_manager.model.enums.TransactionType;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        TransactionType type,
        String icon,
        String color,
        boolean isSystemCategory
) {}
