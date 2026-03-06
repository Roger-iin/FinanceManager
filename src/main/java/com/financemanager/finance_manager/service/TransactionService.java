package com.financemanager.finance_manager.service;

import com.financemanager.finance_manager.dto.transaction.TransactionRequest;
import com.financemanager.finance_manager.dto.transaction.TransactionResponse;
import com.financemanager.finance_manager.mapper.TransactionMapper;
import com.financemanager.finance_manager.model.AccountModel;
import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.TransactionModel;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import com.financemanager.finance_manager.repository.AccountRepository;
import com.financemanager.finance_manager.repository.CategoryRepository;
import com.financemanager.finance_manager.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionResponse> findAll(UserModel user,
                                             LocalDate startDate,
                                             LocalDate endDate,
                                             TransactionType type,
                                             UUID accountId) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now().withDayOfMonth(
                LocalDate.now().lengthOfMonth()
        );

        List<TransactionModel> transactions;

        if (accountId != null && type != null){
            transactions = transactionRepository.findByUserIdAndAccountIdAndDateBetweenOrderByDateDesc(
                    user.getId(), accountId, start, end)
                    .stream()
                    .filter(t -> t.getType() == type)
                    .toList();
        } else if (accountId != null) {
            transactions = transactionRepository.findByUserIdAndAccountIdAndDateBetweenOrderByDateDesc(
                    user.getId(), accountId, start, end);
        } else if (type != null) {
            transactions = transactionRepository.findByUserIdAndTypeAndDateBetweenOrderByDateDesc(
                    user.getId(), type, start, end);
        } else {
            transactions = transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                    user.getId(), start, end);
        }
        return transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public TransactionResponse findById(UUID id, UserModel user){
        TransactionModel transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request, UserModel user){
        AccountModel account = accountRepository.findByIdAndUserId(request.accountId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        CategoryModel category = null;
        if (request.categoryId() != null){
            category = categoryRepository.findByIdAvailableForUser(request.categoryId(), user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        TransactionModel transaction = transactionMapper.toEntity(request, user, account, category);
        TransactionModel saved = transactionRepository.save(transaction);
        return transactionMapper.toResponse(saved);
    }

    @Transactional
    public TransactionResponse update(UUID id, TransactionRequest request, UserModel user){
        TransactionModel transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        AccountModel account = accountRepository.findByIdAndUserId(request.accountId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        CategoryModel category = null;
        if (request.categoryId() != null){
            category = categoryRepository.findByIdAvailableForUser(request.categoryId(), user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }

        transactionMapper.updateEntity(transaction, request, account, category);
        TransactionModel saved = transactionRepository.save(transaction);
        return transactionMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id, UserModel user){
        TransactionModel transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        transactionRepository.delete(transaction);
    }


}
