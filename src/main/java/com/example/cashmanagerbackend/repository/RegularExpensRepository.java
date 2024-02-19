package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.RegularExpens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegularExpensRepository extends JpaRepository<RegularExpens, Integer> {
}