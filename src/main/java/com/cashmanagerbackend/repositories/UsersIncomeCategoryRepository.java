package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.UsersIncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersIncomeCategoryRepository extends JpaRepository<UsersIncomeCategory, UUID> {
}
