package com.example.backend.service;

import com.example.backend.dto.request.CandidatePlaceCreateRequest;
import com.example.backend.dto.response.CandidatePlaceCreateResponse;
import com.example.backend.dto.response.CandidatePlaceResponse;
import com.example.backend.entity.CandidatePlace;
import com.example.backend.entity.Expedition;
import com.example.backend.entity.User;
import com.example.backend.repository.CandidatePlaceRepository;
import com.example.backend.repository.ExpeditionMemberRepository;
import com.example.backend.repository.ExpeditionRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatePlaceService {

    private final CandidatePlaceRepository candidatePlaceRepository;
    private final ExpeditionRepository expeditionRepository;
    private final ExpeditionMemberRepository expeditionMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public CandidatePlaceCreateResponse createCandidatePlace(
            Long userId,
            Long expeditionId,
            CandidatePlaceCreateRequest request
    ) {

        // 1. 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자 정보를 찾을 수 없습니다."
                ));

        // 2. 원정 확인
        Expedition expedition =
                expeditionRepository.findById(expeditionId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "존재하지 않는 원정입니다."
                        ));

        // 3. 해당 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 후보지를 등록할 수 있습니다."
            );
        }

        // 4. 후보지 생성
        CandidatePlace candidatePlace =
                new CandidatePlace(
                        expedition,
                        user,
                        request.kakaoPlaceId(),
                        request.placeName(),
                        request.address(),
                        request.latitude(),
                        request.longitude(),
                        request.memo()
                );

        CandidatePlace saved =
                candidatePlaceRepository.save(candidatePlace);

        // 5. 응답
        return new CandidatePlaceCreateResponse(
                saved.getCandidatePlaceId(),
                expedition.getExpeditionId(),
                saved.getPlaceName(),
                saved.getAddress(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getMemo(),
                user.getUserId(),
                user.getNickname()
        );
    }
    //목록조회
    @Transactional(readOnly = true)
    public List<CandidatePlaceResponse> getCandidatePlaces(
            Long userId,
            Long expeditionId
    ) {

        // 1. 원정 존재 확인
        expeditionRepository.findById(expeditionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 원정입니다."
                ));

        // 2. 해당 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 후보지를 조회할 수 있습니다."
            );
        }

        // 3. 후보지 목록 조회
        List<CandidatePlace> candidatePlaces =
                candidatePlaceRepository
                        .findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
                                expeditionId
                        );

        // 4. Entity -> DTO 변환
        return candidatePlaces.stream()
                .map(candidate -> new CandidatePlaceResponse(
                        candidate.getCandidatePlaceId(),
                        candidate.getPlaceName(),
                        candidate.getAddress(),
                        candidate.getLatitude(),
                        candidate.getLongitude(),
                        candidate.getMemo(),
                        candidate.getCreatedBy().getUserId(),
                        candidate.getCreatedBy().getNickname()
                ))
                .toList();
    }
}