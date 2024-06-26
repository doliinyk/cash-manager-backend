package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.RegularExpense;
import com.cashmanagerbackend.entities.RegularIncome;
import com.cashmanagerbackend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegularIncomeRepository extends JpaRepository<RegularIncome, UUID> {
    Optional<RegularIncome> findByUserAndTitle(User user, String title);

    Page<RegularIncome> findAllByUser(User user, Pageable pageable);

    Optional<RegularIncome> findByUserIdAndTitle(UUID userId, String title);
}