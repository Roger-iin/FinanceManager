package com.financemanager.finance_manager.model;


import com.financemanager.finance_manager.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private UserModel user;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "transaction_type")
    private TransactionType type;

    @Column(nullable = false, length = 50)
    private String icon;

    @Column(nullable = false, length = 7)
    private String color;

    public boolean isSystemCategory(){
        return this.user == null;
    }

}
