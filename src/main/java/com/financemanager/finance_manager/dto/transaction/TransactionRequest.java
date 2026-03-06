package com.financemanager.finance_manager.dto.transaction;

import com.financemanager.finance_manager.model.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequest(

        @NotNull(message = "Conta é obrigatória")
        UUID accountId,

        UUID categoryId,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String description,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "Tipo é obrigatório")
        TransactionType type,

        @NotNull(message = "Data é obrigatória. YYYY-MM-DD")
        LocalDate date,

        String notes
) {}
