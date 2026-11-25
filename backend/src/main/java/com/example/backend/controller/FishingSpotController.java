package com.example.backend.controller;

import com.example.backend.dto.request.FishingSpotCreateRequest;
import com.example.backend.dto.response.FishingSpotCreateResponse;
import com.example.backend.service.FishingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "/api/expeditions/{expeditionId}/candidates/{candidatePlaceId}/spots"
)
@RequiredArgsConstructor
public class FishingSpotController {

    private final FishingSpotService fishingSpotService;

    @PostMapping
    public ResponseEntity<FishingSpotCreateResponse> createFishingSpot(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId,
            @PathVariable Long candidatePlaceId,
            @Valid @RequestBody FishingSpotCreateRequest request
    ) {

        FishingSpotCreateResponse response =
                fishingSpotService.createFishingSpot(
                        userId,
                        expeditionId,
                        candidatePlaceId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}