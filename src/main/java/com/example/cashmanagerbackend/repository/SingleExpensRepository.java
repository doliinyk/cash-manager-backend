package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.SingleExpens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleExpensRepository extends JpaRepository<SingleExpens, Integer> {
}