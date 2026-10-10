package com.example.backend.service;

import com.example.backend.domain.ExpeditionStatus;
import com.example.backend.domain.MemberRole;
import com.example.backend.dto.request.ExpeditionCreateRequest;
import com.example.backend.dto.request.ExpeditionJoinRequest;
import com.example.backend.dto.response.ExpeditionCreateResponse;
import com.example.backend.dto.response.ExpeditionJoinResponse;
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
    //원정코드 초대
    @Transactional
    public ExpeditionJoinResponse joinExpedition(
            Long userId,
            ExpeditionJoinRequest request
    ) {

        // 1. 로그인 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자 정보를 찾을 수 없습니다."
                ));

        // 2. 입력된 초대코드 정리
        String inviteCode =
                request.inviteCode().trim().toUpperCase();

        // 3. 초대코드로 원정 조회
        Expedition expedition =
                expeditionRepository.findByInviteCode(inviteCode)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "존재하지 않는 초대코드입니다."
                        ));

        // 4. 완료 또는 취소된 원정인지 확인
        if (expedition.getStatus() == ExpeditionStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이미 완료된 원정입니다."
            );
        }

        if (expedition.getStatus() == ExpeditionStatus.CANCELED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "취소된 원정입니다."
            );
        }

        // 5. 이미 참가한 사람인지 확인
        if (expeditionMemberRepository
                .existsByExpeditionAndUser(expedition, user)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이미 참가한 원정입니다."
            );
        }

        // 6. MEMBER로 참가
        ExpeditionMember member =
                new ExpeditionMember(
                        expedition,
                        user,
                        MemberRole.MEMBER
                );

        expeditionMemberRepository.save(member);

        // 7. 응답
        return new ExpeditionJoinResponse(
                expedition.getExpeditionId(),
                expedition.getTitle(),
                MemberRole.MEMBER
        );
    }
}