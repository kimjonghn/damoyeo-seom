package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "votes",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"expedition_id", "user_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id", nullable = false)
    private Expedition expedition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_place_id", nullable = false)
    private CandidatePlace candidatePlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Vote(
            Expedition expedition,
            CandidatePlace candidatePlace,
            User user
    ) {
        this.expedition = expedition;
        this.candidatePlace = candidatePlace;
        this.user = user;
    }

    public void changeCandidatePlace(
            CandidatePlace candidatePlace
    ) {
        this.candidatePlace = candidatePlace;
    }
}