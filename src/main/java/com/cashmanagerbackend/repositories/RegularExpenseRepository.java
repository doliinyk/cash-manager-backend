package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.RegularExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegularExpenseRepository extends JpaRepository<RegularExpense, UUID> {
}