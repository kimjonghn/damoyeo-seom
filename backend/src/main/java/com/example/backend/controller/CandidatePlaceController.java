package com.example.backend.controller;

import com.example.backend.dto.request.CandidatePlaceCreateRequest;
import com.example.backend.dto.response.CandidatePlaceCreateResponse;
import com.example.backend.dto.response.CandidatePlaceResponse;
import com.example.backend.service.CandidatePlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expeditions/{expeditionId}/candidates")
@RequiredArgsConstructor
public class CandidatePlaceController {

    private final CandidatePlaceService candidatePlaceService;

    @PostMapping
    public ResponseEntity<CandidatePlaceCreateResponse> createCandidatePlace(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId,
            @Valid @RequestBody CandidatePlaceCreateRequest request
    ) {

        CandidatePlaceCreateResponse response =
                candidatePlaceService.createCandidatePlace(
                        userId,
                        expeditionId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //목록조회
    @GetMapping
    public ResponseEntity<List<CandidatePlaceResponse>> getCandidatePlaces(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId
    ) {

        List<CandidatePlaceResponse> response =
                candidatePlaceService.getCandidatePlaces(
                        userId,
                        expeditionId
                );

        return ResponseEntity.ok(response);
    }
}