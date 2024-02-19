package com.example.cashmanagerbackend.repository;

import com.example.cashmanagerbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}