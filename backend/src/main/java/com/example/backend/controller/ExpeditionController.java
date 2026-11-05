package com.example.backend.controller;

import com.example.backend.dto.request.ExpeditionCreateRequest;
import com.example.backend.dto.request.ExpeditionJoinRequest;
import com.example.backend.dto.response.*;
import com.example.backend.service.ExpeditionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expeditions")
@RequiredArgsConstructor
public class ExpeditionController {

    private final ExpeditionService expeditionService;
    //원정 생성
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
    //원정 참가자 목록 조회
    @GetMapping("/{expeditionId}/members")
    public ResponseEntity<List<ExpeditionMemberResponse>> getExpeditionMembers(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId
    ) {

        List<ExpeditionMemberResponse> response =
                expeditionService.getExpeditionMembers(
                        userId,
                        expeditionId
                );

        return ResponseEntity.ok(response);
    }
    //내가 참가중인 원정 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<MyExpeditionResponse>> getMyExpeditions(
            @AuthenticationPrincipal Long userId
    ) {

        List<MyExpeditionResponse> response =
                expeditionService.getMyExpeditions(userId);

        return ResponseEntity.ok(response);
    }

    //원정 상세 목록 조회
    @GetMapping("/{expeditionId}")
    public ResponseEntity<ExpeditionDetailResponse> getExpeditionDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId
    ) {

        ExpeditionDetailResponse response =
                expeditionService.getExpeditionDetail(
                        userId,
                        expeditionId
                );

        return ResponseEntity.ok(response);
    }
}