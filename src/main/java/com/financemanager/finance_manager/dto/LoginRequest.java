package com.financemanager.finance_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "O e-mail é inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String password

) {}
