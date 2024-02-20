package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.SingleExpens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleExpensRepository extends JpaRepository<SingleExpens, Integer> {
}