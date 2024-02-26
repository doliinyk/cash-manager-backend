package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
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

    @Column(name = "title", nullable = false, length = 50)
    @Size(min = 2, max = 50, message =
            "Title must be between 2 and 50")
    @NotBlank(message = "Title can't be blank")
    private String title;

    @Column(name = "description", length = 500)
    @Size(max = 500, message =
            "Description length can't be more than 500")
    private String description;

    @Column(name = "profit", nullable = false)
    private double profit;

    @Column(name = "last_payment_date", nullable = false)
    private OffsetDateTime lastPaymentDate;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private OffsetDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}