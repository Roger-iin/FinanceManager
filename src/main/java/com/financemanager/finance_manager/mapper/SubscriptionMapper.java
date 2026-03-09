package com.financemanager.finance_manager.mapper;

import com.financemanager.finance_manager.dto.subscription.SubscriptionRequest;
import com.financemanager.finance_manager.dto.subscription.SubscriptionResponse;
import com.financemanager.finance_manager.model.AccountModel;
import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.SubscriptionModel;
import com.financemanager.finance_manager.model.UserModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriptionMapper {
    public SubscriptionResponse toResponse(SubscriptionModel subscription){
        CategoryModel category = subscription.getCategory();

        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getName(),
                subscription.getAmount(),
                subscription.getBillingDay().intValue(),
                subscription.getActive(),
                subscription.getStartDate(),
                subscription.getNotes(),
                subscription.getCreatedAt(),
                subscription.getAccount().getId(),
                subscription.getAccount().getName(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null,
                category != null ? category.getIcon() : null,
                category != null ? category.getColor() : null
        );
    }

    public SubscriptionModel toEntity(SubscriptionRequest request,
                                      UserModel user,
                                      AccountModel account,
                                      CategoryModel category){
        return SubscriptionModel.builder()
                .user(user)
                .account(account)
                .category(category)
                .name(request.name())
                .amount(request.amount())
                .billingDay(request.billingDay().shortValue())
                .active(request.active() != null ? request.active() : true )
                .startDate(request.startDate() != null ? request.startDate() : LocalDate.now())
                .notes(request.notes())
                .build();
    }

    public void updateEntity(SubscriptionModel subscription,
                             SubscriptionRequest request,
                             AccountModel account,
                             CategoryModel category){
        subscription.setAccount(account);
        subscription.setCategory(category);
        subscription.setName(request.name());
        subscription.setAmount(request.amount());
        subscription.setBillingDay(request.billingDay().shortValue());
        if (request.active() != null){
            subscription.setActive(request.active());
        }
        if (request.startDate() != null){
            subscription.setStartDate(request.startDate());
        }
        subscription.setNotes(request.notes());
    }

}
