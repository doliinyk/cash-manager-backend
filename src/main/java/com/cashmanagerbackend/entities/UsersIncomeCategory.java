package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "users_income_categories")
public class UsersIncomeCategory extends BaseEntity implements Comparable<UsersIncomeCategory>{
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private IncomeCategory category;

    @Column(name = "color_code", length = Integer.MAX_VALUE)
    @NotBlank(message = "(Color code is missing")
    private String colorCode;

    @Override
    public int compareTo(UsersIncomeCategory o) {
        return getCategory().getId().compareTo(o.getCategory().getId());
    }
}