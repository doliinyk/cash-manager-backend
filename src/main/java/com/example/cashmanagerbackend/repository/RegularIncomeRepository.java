package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.RegularIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularIncomeRepository extends JpaRepository<RegularIncome, Integer> {
}