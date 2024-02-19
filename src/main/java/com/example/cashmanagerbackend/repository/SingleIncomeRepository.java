package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.SingleIncome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleIncomeRepository extends JpaRepository<SingleIncome, Integer> {
}