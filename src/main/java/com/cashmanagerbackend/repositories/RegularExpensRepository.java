package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.RegularExpens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularExpensRepository extends JpaRepository<RegularExpens, Integer> {
}