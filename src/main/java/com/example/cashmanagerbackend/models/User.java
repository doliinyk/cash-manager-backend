package com.example.cashmanagerbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "login", nullable = false, length = 30)
    private String login;

    @Column(name = "password", nullable = false, length = 90)
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @Column(name = "account", nullable = false)
    private Double account;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @OneToMany(mappedBy = "user")
    private Set<RegularExpens> regularExpenses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<RegularIncome> regularIncomes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SingleExpens> singleExpenses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SingleIncome> singleIncomes = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<ExpenseCategory> expenseCategories = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<IncomeCategory> incomeCategories = new LinkedHashSet<>();

}