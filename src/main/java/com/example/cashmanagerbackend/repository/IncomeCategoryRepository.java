package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Integer> {
}