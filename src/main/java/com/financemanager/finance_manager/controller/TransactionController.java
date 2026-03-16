package com.financemanager.finance_manager.controller;

import com.financemanager.finance_manager.dto.transaction.TransactionRequest;
import com.financemanager.finance_manager.dto.transaction.TransactionResponse;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import com.financemanager.finance_manager.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(required = false) TransactionType type,

            @RequestParam(required = false) UUID accountId,

            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(transactionService.findAll(user, startDate, endDate, type, accountId));
    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionResponse> findById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
    ){
        return ResponseEntity.ok(transactionService.findById(id, user));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserModel user
            ){
        TransactionResponse response = transactionService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable UUID id,
            @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserModel user
    ){
        return ResponseEntity.ok(transactionService.update(id, request, user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @RequestParam UUID id,
            @AuthenticationPrincipal UserModel user
    ){
        transactionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
