package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@Entity
@Table(name = "income_categories")
public class IncomeCategory extends BaseEntity implements Comparable<IncomeCategory> {
    @NotBlank(message = "Title can't be blank")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters long")
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_income_categories",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private SortedSet<User> users = new TreeSet<>();

    @Override
    public int compareTo(IncomeCategory o) {
        return getId().compareTo(o.getId());
    }
}