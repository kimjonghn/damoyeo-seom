package com.example.backend.controller;

import com.example.backend.dto.request.VoteRequest;
import com.example.backend.dto.response.VoteResponse;
import com.example.backend.dto.response.VoteResultResponse;
import com.example.backend.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expeditions/{expeditionId}/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    //투표
    @PostMapping
    public ResponseEntity<VoteResponse> vote(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId,
            @Valid @RequestBody VoteRequest request
    ) {

        VoteResponse response =
                voteService.vote(
                        userId,
                        expeditionId,
                        request
                );

        return ResponseEntity.ok(response);
    }
    //투표 결과 조회
    @GetMapping
    public ResponseEntity<List<VoteResultResponse>> getVoteResults(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId
    ) {

        List<VoteResultResponse> response =
                voteService.getVoteResults(
                        userId,
                        expeditionId
                );

        return ResponseEntity.ok(response);
    }
}
