package com.financemanager.finance_manager.dto.account;

import com.financemanager.finance_manager.model.enums.AccountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AccountRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "Tipo é obrigatório")
        AccountType type,

        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Cor deve estar no formato hexadecimal. Ex: #8A2BE2")
        String color,

        @DecimalMin(value = "0.00", inclusive = true, message = "Saldo não pode ser negativo")
        BigDecimal balance
) {}
