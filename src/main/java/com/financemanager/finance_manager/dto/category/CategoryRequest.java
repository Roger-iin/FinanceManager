package com.financemanager.finance_manager.dto.category;

import com.financemanager.finance_manager.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "Tipo é obrigatorio")
        TransactionType type,

        @Size(max = 50, message = "Icono deve ter no máximo 50 caracteres")
        String icon,

        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Cor deve estar no formato hexadecimal. Ex: #8A2BE2")
        String color
) {}
