package com.financemanager.finance_manager.service;

import com.financemanager.finance_manager.dto.dashboard.CategorySummary;
import com.financemanager.finance_manager.dto.dashboard.DashboardResponse;
import com.financemanager.finance_manager.model.TransactionModel;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.model.enums.TransactionType;
import com.financemanager.finance_manager.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardResponse getSummary(UserModel user, LocalDate startDate, LocalDate endDate){
        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal totalIncome = transactionRepository
                .sumByUserIdAndTypeAndDateBetween(user.getId(), TransactionType.INCOME, start, end);
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;

        BigDecimal totalExpense = transactionRepository
                .sumByUserIdAndTypeAndDateBetween(user.getId(), TransactionType.EXPENSE, start, end);
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;

        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<CategorySummary> expenseByCategory = buildExpenseByCategory(user, start, end, totalExpense);

        return new DashboardResponse(start, end, totalIncome, totalExpense, balance, expenseByCategory);
    }

    private List<CategorySummary> buildExpenseByCategory(UserModel user,
                                                         LocalDate start,
                                                         LocalDate end,
                                                         BigDecimal totalExpense){
        List<TransactionModel> expenses = transactionRepository.findByUserIdAndTypeAndDateBetweenOrderByDateDesc(
                user.getId(), TransactionType.EXPENSE, start, end
        );

        Map<UUID, List<TransactionModel>> groupedByCategory = expenses.stream()
                .filter(t -> t.getCategory() != null)
                .collect(Collectors.groupingBy(t -> t.getCategory().getId()));

        return groupedByCategory.entrySet().stream()
                .map(entry -> {
                    List<TransactionModel> categoryTransactions = entry.getValue();
                    var category = categoryTransactions.get(0).getCategory();

                    BigDecimal total = categoryTransactions.stream()
                            .map(TransactionModel::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal percentage = totalExpense.compareTo(BigDecimal.ZERO) == 0
                            ? BigDecimal.ZERO
                            : total.divide(totalExpense, 4, RoundingMode.HALF_UP)
                                   .multiply(new BigDecimal("100"))
                                   .setScale(2, RoundingMode.HALF_UP);

                    return new CategorySummary(
                            category.getId(),
                            category.getName(),
                            category.getIcon(),
                            category.getColor(),
                            total,
                            percentage
                    );
                }).sorted((a, b) -> b.total().compareTo(a.total()))
                .toList();
    }
}
