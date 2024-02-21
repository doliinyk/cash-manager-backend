package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.SingleExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleExpensRepository extends JpaRepository<SingleExpense, Integer> {
}