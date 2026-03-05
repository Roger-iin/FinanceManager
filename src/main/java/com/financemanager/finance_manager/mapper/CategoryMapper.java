package com.financemanager.finance_manager.mapper;

import com.financemanager.finance_manager.dto.category.CategoryRequest;
import com.financemanager.finance_manager.dto.category.CategoryResponse;
import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(CategoryModel category){
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getIcon(),
                category.getColor(),
                category.isSystemCategory()
        );
    }

    public CategoryModel toEntity(CategoryRequest request, UserModel user){
        return CategoryModel.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .icon(request.icon() != null ? request.icon() : "tag")
                .color(request.color() != null ? request.color() : "#6366f1")
                .build();
    }

    public void updateEntity(CategoryModel category, CategoryRequest request){
        category.setName(request.name());
        category.setType(request.type());
        if (request.icon() != null){
            category.setIcon(request.icon());
        }
        if (request.color() != null){
            category.setColor(request.color());
        }
    }

}
