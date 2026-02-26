package com.financemanager.finance_manager.dto;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String name,
        String email,
        String token
) {}
