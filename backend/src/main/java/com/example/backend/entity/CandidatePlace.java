package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "candidate_places")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidatePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_place_id")
    private Long candidatePlaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id", nullable = false)
    private Expedition expedition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "kakao_place_id")
    private String kakaoPlaceId;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String memo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public CandidatePlace(
            Expedition expedition,
            User createdBy,
            String kakaoPlaceId,
            String placeName,
            String address,
            BigDecimal latitude,
            BigDecimal longitude,
            String memo
    ) {
        this.expedition = expedition;
        this.createdBy = createdBy;
        this.kakaoPlaceId = kakaoPlaceId;
        this.placeName = placeName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
    }
}
