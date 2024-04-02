package com.cashmanagerbackend.repositories;

import com.cashmanagerbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    Optional<User> findByLoginOrEmail(String login, String email);

    Optional<User> findByLoginAndEmail(String login, String email);

    Optional<User> findByEmail(String email);
}