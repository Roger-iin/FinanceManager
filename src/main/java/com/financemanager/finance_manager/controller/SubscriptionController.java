package com.financemanager.finance_manager.controller;

import com.financemanager.finance_manager.dto.subscription.SubscriptionRequest;
import com.financemanager.finance_manager.dto.subscription.SubscriptionResponse;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> findAll(
            @RequestParam(required = false, defaultValue = "false") boolean all,
            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(subscriptionService.findAll(user, !all));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> findByid(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(subscriptionService.findById(id, user));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(
            @Valid @RequestBody SubscriptionRequest request,
            @AuthenticationPrincipal UserModel user
            ){
        SubscriptionResponse response =subscriptionService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody SubscriptionRequest request,
            @AuthenticationPrincipal UserModel user
    ){
        return ResponseEntity.ok(subscriptionService.update(id, request, user));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse> cancel(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
    ){
        return ResponseEntity.ok(subscriptionService.cancel(id, user));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<SubscriptionResponse> reactivate(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
    ){
        return ResponseEntity.ok(subscriptionService.reactivate(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
    ){
        subscriptionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
