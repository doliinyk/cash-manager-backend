package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.IncomeCategory;
import com.cashmanagerbackend.entities.SingleIncome;
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
public interface SingleIncomeRepository extends PagingAndSortingRepository<SingleIncome, UUID>, JpaRepository<SingleIncome, UUID> {
    Page<SingleIncome> findAllByUser(User user, Pageable pageable);

    Page<SingleIncome> findAllByUserAndIncomeDateGreaterThanEqualAndIncomeDateLessThanEqual(User user, OffsetDateTime from, OffsetDateTime to, Pageable pageable);

    Page<SingleIncome> findAllByUserAndIncomeDateGreaterThanEqual(User user, OffsetDateTime from, Pageable pageable);

    Page<SingleIncome> findAllByUserAndIncomeDateLessThanEqual(User user, OffsetDateTime to, Pageable pageable);

    Page<SingleIncome>  findAllByUserAndDescriptionContains(User user, String description, Pageable pageable);

    Optional<SingleIncome> findByIdAndUser(UUID id, User user);

    Page<SingleIncome> findAllByUserIdAndCategory(UUID id, IncomeCategory incomeCategory, Pageable pageable);

    Page<SingleIncome> findAllByUserAndProfitGreaterThanEqualAndProfitLessThanEqual(User user, Double from, Double aDouble, Pageable pageable);

    Page<SingleIncome> findAllByUserAndProfitGreaterThanEqual(User user, Double from, Pageable pageable);

    Page<SingleIncome> findAllByUserAndProfitLessThanEqual(User user, Double aDouble, Pageable pageable);
}