package com.financemanager.finance_manager.repository;

import com.financemanager.finance_manager.model.CategoryModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {

    @Query("SELECT c FROM CategoryModel c WHERE c.user IS NULL OR c.user.id = :userId")
    List<CategoryModel> findAllAvaliableForUser(@Param("userId") UUID userId);

    @Query("""
            SELECT c FROM CategoryModel c WHERE (c.user IS NULL OR c.user.id = :userId)
            AND c.type = :type
            """)
    List<CategoryModel> findAllAvaliableForUserByType(
            @Param("userId") UUID userId,
            @Param("type")TransactionType type
    );

    @Query("""
            SELECT c FROM CategoryModel c 
            WHERE c.id = :id
            AND (c.user IS NULL or c.user.id = :userId) 
            """)
    Optional<CategoryModel> findByIdAvailableForUser(
            @Param("id") UUID id,
            @Param("userId") UUID userId
    );

}
