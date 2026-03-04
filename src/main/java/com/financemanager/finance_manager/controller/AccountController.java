package com.financemanager.finance_manager.controller;

import com.financemanager.finance_manager.dto.account.AccountRequest;
import com.financemanager.finance_manager.dto.account.AccountResponse;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll(@AuthenticationPrincipal UserModel user){
        return ResponseEntity.ok(accountService.findAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(accountService.findById(id, user));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody AccountRequest request,
            @AuthenticationPrincipal UserModel user
            ){
        AccountResponse response = accountService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(
            @PathVariable UUID id,
            @RequestBody AccountRequest request,
            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(accountService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
            ){
        accountService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
