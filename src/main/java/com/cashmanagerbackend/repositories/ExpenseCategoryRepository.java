package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.ExpenseCategory;
import com.cashmanagerbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID> {
    Optional<ExpenseCategory> findByTitle(String title);
}