package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "regular_incomes")
public class RegularIncome extends BaseEntity {
    @Column(name = "periodicity", nullable = false)
    private long periodicity;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters long")
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Size(max = 500, message = "Description length can't be more than 500 characters long")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "profit", nullable = false)
    private double profit;

    @Column(name = "last_payment_date", nullable = false)
    private OffsetDateTime lastPaymentDate;

    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private IncomeCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}