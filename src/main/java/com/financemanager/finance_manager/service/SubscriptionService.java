package com.financemanager.finance_manager.service;

import com.financemanager.finance_manager.dto.subscription.SubscriptionRequest;
import com.financemanager.finance_manager.dto.subscription.SubscriptionResponse;
import com.financemanager.finance_manager.mapper.SubscriptionMapper;
import com.financemanager.finance_manager.model.AccountModel;
import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.SubscriptionModel;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.repository.AccountRepository;
import com.financemanager.finance_manager.repository.CategoryRepository;
import com.financemanager.finance_manager.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.subscriptionMapper = subscriptionMapper;
    }

    public List<SubscriptionResponse> findAll(UserModel user, boolean onlyActive){
        List<SubscriptionModel> subscriptions = onlyActive
                ? subscriptionRepository.findByUserIdAndActiveTrue(user.getId())
                : subscriptionRepository.findByUserId(user.getId());

        return subscriptions.stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    public SubscriptionResponse findById(UUID id, UserModel user){
        SubscriptionModel subscription = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        return subscriptionMapper.toResponse(subscription);
    }

    @Transactional
    public SubscriptionResponse create(SubscriptionRequest request, UserModel user){
        AccountModel account = accountRepository.findByIdAndUserId(request.accountId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        CategoryModel category = null;
        if (category != null){
            category = categoryRepository.findByIdAvailableForUser(request.categoryId(), user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }
        SubscriptionModel subscription = subscriptionMapper.toEntity(request, user, account, category);
        SubscriptionModel saved = subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponse update(UUID id, SubscriptionRequest request, UserModel user){
        SubscriptionModel subscription = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));
        AccountModel account = accountRepository.findByIdAndUserId(request.accountId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        CategoryModel category = null;
        if (category != null){
            category = categoryRepository.findByIdAvailableForUser(request.categoryId(), user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        }
        subscriptionMapper.updateEntity(subscription, request, account, category);
        SubscriptionModel saved = subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponse cancel(UUID id, UserModel user){
        SubscriptionModel subscription = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        subscription.setActive(false);
        SubscriptionModel saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponse reactivate(UUID id, UserModel user){
        SubscriptionModel subscription = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        subscription.setActive(true);
        SubscriptionModel saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id, UserModel user){
        SubscriptionModel subscription = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        subscriptionRepository.delete(subscription);
    }

}
