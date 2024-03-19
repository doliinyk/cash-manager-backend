package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "single_expenses")
public class SingleExpense extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;

    @Column(name = "description", length = 500)
    @Size(max = 500, message = "Description length can't be more than 500")
    private String description;

    @Column(name = "cost", nullable = false)
    private double cost;

    @NotNull
    @Column(name = "expenses_date", nullable = false)
    private OffsetDateTime expensesDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}