package com.example.backend.service;

import com.example.backend.domain.MemberRole;
import com.example.backend.dto.request.ExpeditionCreateRequest;
import com.example.backend.dto.response.ExpeditionCreateResponse;
import com.example.backend.entity.Expedition;
import com.example.backend.entity.ExpeditionMember;
import com.example.backend.entity.User;
import com.example.backend.repository.ExpeditionMemberRepository;
import com.example.backend.repository.ExpeditionRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ExpeditionService {

    private static final String INVITE_CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final int INVITE_CODE_LENGTH = 6;

    private final ExpeditionRepository expeditionRepository;
    private final ExpeditionMemberRepository expeditionMemberRepository;
    private final UserRepository userRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public ExpeditionCreateResponse createExpedition(
            Long userId,
            ExpeditionCreateRequest request
    ) {

        // 1. 로그인 사용자 확인
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자 정보를 찾을 수 없습니다."
                ));

        // 2. 날짜 검증
        if (request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "복귀 날짜는 출발 날짜보다 빠를 수 없습니다."
            );
        }

        // 3. 초대코드 생성
        String inviteCode = generateInviteCode();

        // 4. 원정 생성
        Expedition expedition = new Expedition(
                owner,
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.departureLocation(),
                request.expectedMemberCount(),
                request.vehicleCount(),
                inviteCode
        );

        Expedition savedExpedition =
                expeditionRepository.save(expedition);

        // 5. 방장을 참가자로 자동 등록
        ExpeditionMember leader =
                new ExpeditionMember(
                        savedExpedition,
                        owner,
                        MemberRole.LEADER
                );

        expeditionMemberRepository.save(leader);

        // 6. 응답
        return new ExpeditionCreateResponse(
                savedExpedition.getExpeditionId(),
                savedExpedition.getTitle(),
                savedExpedition.getStartDate(),
                savedExpedition.getEndDate(),
                savedExpedition.getDepartureLocation(),
                savedExpedition.getExpectedMemberCount(),
                savedExpedition.getVehicleCount(),
                savedExpedition.getInviteCode(),
                savedExpedition.getStatus()
        );
    }

    private String generateInviteCode() {

        String inviteCode;

        do {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < INVITE_CODE_LENGTH; i++) {
                int index =
                        secureRandom.nextInt(INVITE_CHARACTERS.length());

                builder.append(
                        INVITE_CHARACTERS.charAt(index)
                );
            }

            inviteCode = builder.toString();

        } while (expeditionRepository.existsByInviteCode(inviteCode));

        return inviteCode;
    }
}