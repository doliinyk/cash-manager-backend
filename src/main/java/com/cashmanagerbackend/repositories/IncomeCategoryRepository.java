package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.IncomeCategory;
import com.cashmanagerbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, UUID> {
    Optional<IncomeCategory> findByTitle(String title);

    @Query("SELECT i_c FROM IncomeCategory i_c JOIN i_c.users u WHERE u = ?1 AND i_c.title = ?2")
    Optional<IncomeCategory> findCategoryInUserByTitle(User user, String title);
}