package com.example.backend.entity;

import com.example.backend.domain.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "expedition_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpeditionMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expedition_member_id")
    private Long expeditionMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id", nullable = false)
    private Expedition expedition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @Column(name = "joined_at", insertable = false, updatable = false)
    private LocalDateTime joinedAt;

    public ExpeditionMember(
            Expedition expedition,
            User user,
            MemberRole role
    ) {
        this.expedition = expedition;
        this.user = user;
        this.role = role;
    }
}