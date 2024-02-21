package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.RegularIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularIncomeRepository extends JpaRepository<RegularIncome, Integer> {
}