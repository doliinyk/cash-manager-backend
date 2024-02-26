package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "login", nullable = false, length = 30)
    @Size(min = 8, max = 20, message
            = "Login must be between 8 and 20")
    @NotBlank(message = "Login is missing")
    private String login;

    @Column(name = "password", nullable = false, length = 90)
    @NotBlank(message = "Password is missing")
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is missing")
    private String email;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private OffsetDateTime createDate;

    @Column(name = "account", nullable = false)
    private double account;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @OneToMany(mappedBy = "user")
    private Set<RegularExpense> regularExpenses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<RegularIncome> regularIncomes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SingleExpense> singleExpenses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SingleIncome> singleIncomes = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<ExpenseCategory> expenseCategories = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<IncomeCategory> incomeCategories = new LinkedHashSet<>();
}