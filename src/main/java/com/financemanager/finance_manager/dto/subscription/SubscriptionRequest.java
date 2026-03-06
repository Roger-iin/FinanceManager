package com.financemanager.finance_manager.dto.subscription;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionRequest(

        @NotNull(message = "Conta é obrigatória")
        UUID accountId,

        UUID categoryId,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "valor deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "Dia de cobrança é obrigatório")
        @Min(value = 1, message = "Dia de cobrança deve ser entre 1 e 31")
        @Max(value = 31, message = "Dia de cobrança deve ser entre 1 e 31")
        Integer billingDay,

        Boolean active,

        LocalDate startDate,

        String notes
) {}
