package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID uuid);
    Optional<User> findByEmail(String email);
}