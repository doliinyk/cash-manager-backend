package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.UsersIncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersIncomeCategoryRepository extends JpaRepository<UsersIncomeCategory, UUID> {
}
