package com.example.cashmanagerbackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "regular_expenses")
public class RegularExpens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenses_id", nullable = false)
    private Integer id;

    @Column(name = "periodicity", nullable = false)
    private Long periodicity;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "last_payment_date", nullable = false)
    private OffsetDateTime lastPaymentDate;

    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}