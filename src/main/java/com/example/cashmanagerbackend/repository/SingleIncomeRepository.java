package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.SingleIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleIncomeRepository extends JpaRepository<SingleIncome, Integer> {
}