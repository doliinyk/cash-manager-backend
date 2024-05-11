package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.SingleExpense;
import com.cashmanagerbackend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SingleExpenseRepository extends PagingAndSortingRepository<SingleExpense, UUID>, JpaRepository<SingleExpense, UUID> {
    Page<SingleExpense> findAllByUser(User user, Pageable pageable);

    Page<SingleExpense> findAllByUserAndExpensesDateGreaterThanEqualAndExpensesDateLessThanEqual(User user, OffsetDateTime from, OffsetDateTime to, Pageable pageable);

    Page<SingleExpense> findAllByUserAndExpensesDateGreaterThanEqual(User user, OffsetDateTime from, Pageable pageable);

    Page<SingleExpense> findAllByUserAndExpensesDateLessThanEqual(User user, OffsetDateTime to, Pageable pageable);

    Page<SingleExpense>  findAllByUserAndDescriptionContains(User user, String description, Pageable pageable);

    Optional<SingleExpense> findByIdAndUser(UUID id, User user);
}