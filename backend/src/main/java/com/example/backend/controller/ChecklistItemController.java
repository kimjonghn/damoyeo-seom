package com.example.backend.controller;

import com.example.backend.dto.request.ChecklistAssigneeUpdateRequest;
import com.example.backend.dto.request.ChecklistItemCreateRequest;
import com.example.backend.dto.response.ChecklistItemCreateResponse;
import com.example.backend.dto.response.ChecklistItemResponse;
import com.example.backend.service.ChecklistItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expeditions/{expeditionId}/checklist")
@RequiredArgsConstructor
public class ChecklistItemController {

    private final ChecklistItemService checklistItemService;

    @PostMapping
    public ResponseEntity<ChecklistItemCreateResponse> createChecklistItem(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId,
            @Valid @RequestBody ChecklistItemCreateRequest request
    ) {

        ChecklistItemCreateResponse response =
                checklistItemService.createChecklistItem(
                        userId,
                        expeditionId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChecklistItemResponse>> getChecklistItems(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId
    ) {

        List<ChecklistItemResponse> response =
                checklistItemService.getChecklistItems(
                        userId,
                        expeditionId
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{checklistItemId}/assignee")
    public ResponseEntity<ChecklistItemResponse> updateAssignee(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId,
            @PathVariable Long checklistItemId,
            @RequestBody ChecklistAssigneeUpdateRequest request
    ) {

        ChecklistItemResponse response =
                checklistItemService.updateAssignee(
                        userId,
                        expeditionId,
                        checklistItemId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}