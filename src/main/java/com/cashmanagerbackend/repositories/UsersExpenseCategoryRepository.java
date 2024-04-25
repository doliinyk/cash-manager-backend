package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.ExpenseCategory;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.entities.UsersExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;

@Repository
public interface UsersExpenseCategoryRepository extends JpaRepository<UsersExpenseCategory, UUID> {
    SortedSet<UsersExpenseCategory> findAllByUser(User user);

    Optional<UsersExpenseCategory> findByUserAndCategory(User user, ExpenseCategory expenseCategory);
}
