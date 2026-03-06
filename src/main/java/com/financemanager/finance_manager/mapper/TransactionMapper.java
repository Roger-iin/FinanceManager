package com.financemanager.finance_manager.mapper;

import com.financemanager.finance_manager.dto.transaction.TransactionRequest;
import com.financemanager.finance_manager.dto.transaction.TransactionResponse;
import com.financemanager.finance_manager.model.AccountModel;
import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.TransactionModel;
import com.financemanager.finance_manager.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(TransactionModel transaction){
        CategoryModel category = transaction.getCategory();

        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getNotes(),
                transaction.getCreatedAt(),
                transaction.getAccount().getId(),
                transaction.getAccount().getName(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null,
                category != null ? category.getIcon() : null,
                category != null ? category.getColor() : null
        );
    }

    public TransactionModel toEntity(TransactionRequest request,
                                     UserModel user,
                                     AccountModel account,
                                     CategoryModel category){
        return TransactionModel.builder()
                .user(user)
                .account(account)
                .category(category)
                .description(request.description())
                .amount(request.amount())
                .type(request.type())
                .date(request.date())
                .notes(request.notes())
                .build();
    }

    public void updateEntity(TransactionModel transaction,
                             TransactionRequest request,
                             AccountModel account,
                             CategoryModel category) {
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setDate(request.date());
        transaction.setNotes(request.notes());
    }

}
