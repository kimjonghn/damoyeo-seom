package com.example.backend.controller;

import com.example.backend.dto.request.ExpeditionCreateRequest;
import com.example.backend.dto.request.ExpeditionJoinRequest;
import com.example.backend.dto.response.ExpeditionCreateResponse;
import com.example.backend.dto.response.ExpeditionJoinResponse;
import com.example.backend.service.ExpeditionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expeditions")
@RequiredArgsConstructor
public class ExpeditionController {

    private final ExpeditionService expeditionService;

    @PostMapping
    public ResponseEntity<ExpeditionCreateResponse> createExpedition(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ExpeditionCreateRequest request
    ) {

        ExpeditionCreateResponse response =
                expeditionService.createExpedition(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    //초대 코드로 원정참가
    @PostMapping("/join")
    public ResponseEntity<ExpeditionJoinResponse> joinExpedition(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ExpeditionJoinRequest request
    ) {

        ExpeditionJoinResponse response =
                expeditionService.joinExpedition(userId, request);

        return ResponseEntity.ok(response);
    }
}