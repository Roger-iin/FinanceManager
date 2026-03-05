package com.financemanager.finance_manager.controller;

import com.financemanager.finance_manager.dto.category.CategoryRequest;
import com.financemanager.finance_manager.dto.category.CategoryResponse;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import com.financemanager.finance_manager.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listAll(
            @RequestParam(required = false)TransactionType type,
            @AuthenticationPrincipal UserModel user
            ){
        List<CategoryResponse> response = categoryService.findAll(user, type);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request,
            @AuthenticationPrincipal UserModel user
            ){
        CategoryResponse response = categoryService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request,
            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(categoryService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserModel user
            ){
        categoryService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

}
