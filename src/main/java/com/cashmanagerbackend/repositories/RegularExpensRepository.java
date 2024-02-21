package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.RegularExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularExpensRepository extends JpaRepository<RegularExpense, Integer> {
}