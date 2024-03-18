package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, UUID> {
}