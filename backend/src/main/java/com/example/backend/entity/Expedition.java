package com.example.backend.entity;

import com.example.backend.domain.ExpeditionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expeditions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expedition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expedition_id")
    private Long expeditionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "departure_location", length = 255)
    private String departureLocation;

    @Column(name = "expected_member_count")
    private Integer expectedMemberCount;

    @Column(name = "vehicle_count", nullable = false)
    private Integer vehicleCount;

    @Column(name = "invite_code", nullable = false, unique = true, length = 30)
    private String inviteCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExpeditionStatus status;

    @Column(name = "selected_candidate_id")
    private Long selectedCandidateId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Expedition(
            User owner,
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            String departureLocation,
            Integer expectedMemberCount,
            Integer vehicleCount,
            String inviteCode
    ) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.departureLocation = departureLocation;
        this.expectedMemberCount = expectedMemberCount;
        this.vehicleCount = vehicleCount;
        this.inviteCode = inviteCode;
        this.status = ExpeditionStatus.PLANNING;
    }
}