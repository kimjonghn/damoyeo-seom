package com.example.backend.service;

import com.example.backend.dto.request.FishingSpotCreateRequest;
import com.example.backend.dto.response.FishingSpotCreateResponse;
import com.example.backend.entity.CandidatePlace;
import com.example.backend.entity.FishingSpot;
import com.example.backend.entity.User;
import com.example.backend.repository.CandidatePlaceRepository;
import com.example.backend.repository.ExpeditionMemberRepository;
import com.example.backend.repository.FishingSpotRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FishingSpotService {

    private final FishingSpotRepository fishingSpotRepository;
    private final CandidatePlaceRepository candidatePlaceRepository;
    private final ExpeditionMemberRepository expeditionMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public FishingSpotCreateResponse createFishingSpot(
            Long userId,
            Long expeditionId,
            Long candidatePlaceId,
            FishingSpotCreateRequest request
    ) {

        // 1. 로그인 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자 정보를 찾을 수 없습니다."
                ));

        // 2. 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 낚시 포인트를 등록할 수 있습니다."
            );
        }

        // 3. 해당 후보지가 이 원정의 후보지인지 확인
        CandidatePlace candidatePlace =
                candidatePlaceRepository
                        .findByCandidatePlaceIdAndExpedition_ExpeditionId(
                                candidatePlaceId,
                                expeditionId
                        )
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "해당 원정에 존재하지 않는 후보지입니다."
                        ));

        // 4. 수심값 검증
        if (request.depthMin() != null
                && request.depthMax() != null
                && request.depthMin().compareTo(request.depthMax()) > 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "최소 수심은 최대 수심보다 클 수 없습니다."
            );
        }

        // 5. 포인트 생성
        FishingSpot fishingSpot =
                new FishingSpot(
                        candidatePlace,
                        user,
                        request.name(),
                        request.latitude(),
                        request.longitude(),
                        request.depthMin(),
                        request.depthMax(),
                        request.targetSpecies(),
                        request.fishingMethods(),
                        request.memo()
                );

        FishingSpot saved =
                fishingSpotRepository.save(fishingSpot);

        return new FishingSpotCreateResponse(
                saved.getFishingSpotId(),
                candidatePlace.getCandidatePlaceId(),
                candidatePlace.getPlaceName(),
                saved.getName(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getDepthMin(),
                saved.getDepthMax(),
                saved.getTargetSpecies(),
                saved.getFishingMethods(),
                saved.getMemo(),
                user.getUserId(),
                user.getNickname()
        );
    }
}