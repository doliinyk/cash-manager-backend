package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.RegularIncome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegularIncomeRepository extends JpaRepository<RegularIncome, Integer> {
}