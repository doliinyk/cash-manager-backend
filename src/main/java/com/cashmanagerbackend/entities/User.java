package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    @Column(name = "login", nullable = false, length = 30)
    @Size(min = 3, max = 30, message
            = "Login must be between 3 and 30")
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
    private LocalDateTime createDate;

    @Column(name = "account", nullable = false)
    private double account;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Column(name = "activation_refresh_uuid")
    private UUID activationRefreshUUID;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() { return deleteDate == null;}

    @Override
    public boolean isAccountNonLocked() {
        return deleteDate == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return deleteDate == null;
    }

    @Override
    public boolean isEnabled() {
        return (deleteDate == null && activated);
    }
}