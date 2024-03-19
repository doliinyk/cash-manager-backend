package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.SingleIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SingleIncomeRepository extends JpaRepository<SingleIncome, UUID> {
}