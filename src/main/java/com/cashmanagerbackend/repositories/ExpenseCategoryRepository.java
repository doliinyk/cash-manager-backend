package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.ExpenseCategory;
import com.cashmanagerbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID> {
    Optional<ExpenseCategory> findByTitle(String title);

    @Query("SELECT e_c FROM ExpenseCategory e_c JOIN e_c.users u WHERE u = ?1 AND e_c.title = ?2")
    Optional<ExpenseCategory> findCategoryInUserByTitle(User user, String title);
}