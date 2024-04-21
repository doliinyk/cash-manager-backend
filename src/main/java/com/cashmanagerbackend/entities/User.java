package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity implements UserDetails, Comparable<User>{
    @NotBlank(message = "Login is missing")
    @Pattern(regexp = "^(?=.*[a-zA-Z])\\w{3,30}$",
            message = "Login must contain at least one letter and can contain numbers or underscore from 3 to 30 symbols")
    @Column(name = "login", nullable = false, length = 30)
    private String login;

    @NotBlank(message = "Password is missing")
    @Column(name = "password", nullable = false, length = 90)
    private String password;

    @NotBlank(message = "Email is missing")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @CreatedDate
    @Column(name = "create_date", nullable = false)
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

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private SortedSet<ExpenseCategory> expenseCategories = new TreeSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private SortedSet<IncomeCategory> incomeCategories = new TreeSet<>();

    @OneToMany(mappedBy = "user")
    private transient Set<RegularExpense> regularExpenses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private transient Set<RegularIncome> regularIncomes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private transient Set<SingleExpense> singleExpenses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private transient Set<SingleIncome> singleIncomes = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return deleteDate == null;
    }

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

    @Override
    public int compareTo(User o) {
        return this.getId().compareTo(o.getId());
    }
}