package com.financemanager.finance_manager.service;

import com.financemanager.finance_manager.dto.category.CategoryRequest;
import com.financemanager.finance_manager.dto.category.CategoryResponse;
import com.financemanager.finance_manager.mapper.CategoryMapper;
import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import com.financemanager.finance_manager.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> findAll(UserModel user){
        return categoryRepository.findAllAvaliableForUser(user.getId())
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public List<CategoryResponse> findAllByType(UserModel user, TransactionType type){
        return categoryRepository.findAllAvaliableForUserByType(user.getId(), type)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request, UserModel user){
        CategoryModel category = categoryMapper.toEntity(request, user);
        CategoryModel saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request, UserModel user){
        CategoryModel category = categoryRepository.findByIdAvailableForUser(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        if (category.isSystemCategory()){
            throw new IllegalArgumentException("Categorias padrão do sistema não podem ser editadas");
        }

        categoryMapper.updateEntity(category, request);
        CategoryModel saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id, UserModel user){
        CategoryModel category = categoryRepository.findByIdAvailableForUser(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        if (category.isSystemCategory()){
            throw new IllegalArgumentException("Categorias padrão do sistema não podem ser deletadas");
        }

        categoryRepository.delete(category);
    }


}
