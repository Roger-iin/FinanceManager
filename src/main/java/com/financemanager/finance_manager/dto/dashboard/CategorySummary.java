package com.financemanager.finance_manager.dto.dashboard;

import java.math.BigDecimal;
import java.util.UUID;

public record CategorySummary(
        UUID categoryId,
        String categoryName,
        String categoryIcon,
        String categoryColor,
        BigDecimal total,
        BigDecimal percentage
) {}
