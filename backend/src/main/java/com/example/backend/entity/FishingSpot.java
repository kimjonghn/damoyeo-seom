package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "fishing_spots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FishingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fishing_spot_id")
    private Long fishingSpotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_place_id", nullable = false)
    private CandidatePlace candidatePlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "depth_min")
    private BigDecimal depthMin;

    @Column(name = "depth_max")
    private BigDecimal depthMax;

    @Column(name = "target_species")
    private String targetSpecies;

    @Column(name = "fishing_methods")
    private String fishingMethods;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public FishingSpot(
            CandidatePlace candidatePlace,
            User createdBy,
            String name,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal depthMin,
            BigDecimal depthMax,
            String targetSpecies,
            String fishingMethods,
            String memo
    ) {
        this.candidatePlace = candidatePlace;
        this.createdBy = createdBy;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depthMin = depthMin;
        this.depthMax = depthMax;
        this.targetSpecies = targetSpecies;
        this.fishingMethods = fishingMethods;
        this.memo = memo;
    }
}