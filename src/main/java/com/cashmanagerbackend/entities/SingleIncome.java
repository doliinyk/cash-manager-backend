package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "single_incomes")
public class SingleIncome extends BaseEntity {
    @NotNull(message = "Category can't be null")
    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private IncomeCategory category;
    
    @Size(max = 500, message = "Description length can't be more than 500 characters long")
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "profit", nullable = false)
    private double profit;
    
    @Column(name = "income_date", nullable = false)
    private OffsetDateTime incomeDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}