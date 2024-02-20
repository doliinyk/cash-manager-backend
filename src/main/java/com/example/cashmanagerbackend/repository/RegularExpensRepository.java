package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.RegularExpens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularExpensRepository extends JpaRepository<RegularExpens, Integer> {
}