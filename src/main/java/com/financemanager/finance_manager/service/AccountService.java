package com.financemanager.finance_manager.service;

import com.financemanager.finance_manager.dto.account.AccountRequest;
import com.financemanager.finance_manager.dto.account.AccountResponse;
import com.financemanager.finance_manager.mapper.AccountMapper;
import com.financemanager.finance_manager.model.AccountModel;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountResponse> findAll(UserModel user){
        return accountRepository.findByUserId(user.getId())
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    public AccountResponse findById(UUID id, UserModel user) {
        AccountModel account = accountRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponse create(AccountRequest request, UserModel user){
        AccountModel account = accountMapper.toEntity(request, user);
        AccountModel saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    @Transactional
    public AccountResponse update(UUID id, AccountRequest request, UserModel user){
        AccountModel account = accountRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        accountMapper.updateEntity(account, request);
        AccountModel saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id, UserModel user){
        if (!accountRepository.existsByIdAndUserId(id, user.getId())){
            throw new EntityNotFoundException("Conta não encontrada");
        }
        accountRepository.deleteById(id);
    }
}
