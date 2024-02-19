package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {
}