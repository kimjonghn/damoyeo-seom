package com.example.backend.service;

import com.example.backend.dto.request.VoteRequest;
import com.example.backend.dto.response.VoteResponse;
import com.example.backend.dto.response.VoteResultResponse;
import com.example.backend.entity.CandidatePlace;
import com.example.backend.entity.Expedition;
import com.example.backend.entity.User;
import com.example.backend.entity.Vote;
import com.example.backend.repository.CandidatePlaceRepository;
import com.example.backend.repository.ExpeditionMemberRepository;
import com.example.backend.repository.ExpeditionRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final CandidatePlaceRepository candidatePlaceRepository;
    private final ExpeditionRepository expeditionRepository;
    private final ExpeditionMemberRepository expeditionMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public VoteResponse vote(
            Long userId,
            Long expeditionId,
            VoteRequest request
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

        // 3. 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 투표할 수 있습니다."
            );
        }

        // 4. 해당 원정의 후보지인지 확인
        CandidatePlace candidatePlace =
                candidatePlaceRepository
                        .findByCandidatePlaceIdAndExpedition_ExpeditionId(
                                request.candidatePlaceId(),
                                expeditionId
                        )
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "해당 원정에 존재하지 않는 후보지입니다."
                        ));

        // 5. 기존 투표 확인
        Vote vote =
                voteRepository
                        .findByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        )
                        .orElse(null);

        if (vote == null) {

            // 최초 투표
            vote = new Vote(
                    expedition,
                    candidatePlace,
                    user
            );

        } else {

            // 기존 투표 변경
            vote.changeCandidatePlace(candidatePlace);
        }

        Vote savedVote =
                voteRepository.save(vote);

        return new VoteResponse(
                savedVote.getVoteId(),
                expedition.getExpeditionId(),
                candidatePlace.getCandidatePlaceId(),
                candidatePlace.getPlaceName(),
                user.getUserId(),
                user.getNickname()
        );
    }

    //투표 결과 조회
    @Transactional(readOnly = true)
    public List<VoteResultResponse> getVoteResults(
            Long userId,
            Long expeditionId
    ) {

        // 1. 원정 존재 확인
        expeditionRepository.findById(expeditionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 원정입니다."
                ));

        // 2. 로그인 사용자가 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 투표 결과를 조회할 수 있습니다."
            );
        }

        // 3. 원정의 후보지 목록
        List<CandidatePlace> candidates =
                candidatePlaceRepository
                        .findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
                                expeditionId
                        );

        // 4. 원정의 전체 투표 조회
        List<Vote> votes =
                voteRepository
                        .findAllByExpedition_ExpeditionId(
                                expeditionId
                        );

        // 5. 각 후보지별 투표 수 계산
        return candidates.stream()
                .map(candidate -> {

                    long voteCount = votes.stream()
                            .filter(vote ->
                                    vote.getCandidatePlace()
                                            .getCandidatePlaceId()
                                            .equals(candidate.getCandidatePlaceId())
                            )
                            .count();

                    boolean votedByMe = votes.stream()
                            .anyMatch(vote ->
                                    vote.getUser().getUserId().equals(userId)
                                            &&
                                            vote.getCandidatePlace()
                                                    .getCandidatePlaceId()
                                                    .equals(candidate.getCandidatePlaceId())
                            );

                    return new VoteResultResponse(
                            candidate.getCandidatePlaceId(),
                            candidate.getPlaceName(),
                            voteCount,
                            votedByMe
                    );
                })
                .toList();
    }
}