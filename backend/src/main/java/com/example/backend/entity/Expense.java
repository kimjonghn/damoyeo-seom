package com.example.backend.entity;

import com.example.backend.domain.ExpenseCalculationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "expenses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//비용관리
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expenseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id", nullable = false)
    private Expedition expedition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_type", nullable = false)
    private ExpenseCalculationType calculationType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Expense(
            Expedition expedition,
            User createdBy,
            String name,
            BigDecimal amount,
            ExpenseCalculationType calculationType,
            Integer quantity
    ) {
        this.expedition = expedition;
        this.createdBy = createdBy;
        this.name = name;
        this.amount = amount;
        this.calculationType = calculationType;
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return amount.multiply(BigDecimal.valueOf(quantity));
    }
}