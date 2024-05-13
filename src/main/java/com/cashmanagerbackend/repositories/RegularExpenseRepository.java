package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.RegularExpense;
import com.cashmanagerbackend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegularExpenseRepository extends JpaRepository<RegularExpense, UUID> {
    Optional<RegularExpense> findByUserAndTitle(User user, String title);

    Page<RegularExpense> findAllByUser(User user, Pageable pageable);

//    Optional<RegularExpense>

    Optional<RegularExpense> findByUserIdAndTitle(UUID userId, String title);
}