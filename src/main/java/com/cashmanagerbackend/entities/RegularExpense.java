package com.cashmanagerbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "regular_expenses")
@EntityListeners(AuditingEntityListener.class)
public class RegularExpense extends BaseEntity {
    @Column(name = "periodicity", nullable = false)
    private long periodicity;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters long")
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Size(max = 500, message = "Description length can't be more than 500 characters long")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "last_payment_date")
    private OffsetDateTime lastPaymentDate;

    @CreatedDate
    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}