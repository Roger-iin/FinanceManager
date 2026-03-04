package com.financemanager.finance_manager.mapper;

import com.financemanager.finance_manager.dto.account.AccountRequest;
import com.financemanager.finance_manager.dto.account.AccountResponse;
import com.financemanager.finance_manager.model.AccountModel;
import com.financemanager.finance_manager.model.UserModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountMapper {

    public AccountResponse toResponse(AccountModel account){
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getColor(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }

    public AccountModel toEntity(AccountRequest request, UserModel user){
        return AccountModel.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .color(request.color() != null ? request.color() : "#6366f1")
                .balance(request.balance() != null ? request.balance() : BigDecimal.ZERO)
                .build();
    }

    public void updateEntity(AccountModel account, AccountRequest request){
        account.setName(request.name());
        account.setType(request.type());
        if (request.color() != null){
            account.setColor(request.color());
        }
        if (request.balance() != null){
            account.setBalance(request.balance());
        }
    }

}
