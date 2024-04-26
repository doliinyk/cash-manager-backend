package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;

@Repository
public interface UsersIncomeCategoryRepository extends JpaRepository<UsersIncomeCategory, UUID> {
    SortedSet<UsersIncomeCategory> findAllByUser(User user);

    Optional<UsersIncomeCategory> findByUserAndCategory(User user, IncomeCategory incomeCategory);
}
