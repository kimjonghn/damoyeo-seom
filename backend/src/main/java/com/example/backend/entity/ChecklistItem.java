package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "checklist_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_item_id")
    private Long checklistItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id", nullable = false)
    private Expedition expedition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public ChecklistItem(
            Expedition expedition,
            User createdBy,
            User assignedUser,
            String name
    ) {
        this.expedition = expedition;
        this.createdBy = createdBy;
        this.assignedUser = assignedUser;
        this.name = name;
        this.isChecked = false;
    }
    public void assignUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public void updateChecked(boolean checked) {
        this.isChecked = checked;

        if (checked) {
            this.checkedAt = LocalDateTime.now();
        } else {
            this.checkedAt = null;
        }
    }
}