package com.example.backend.service;

import com.example.backend.dto.request.ChecklistAssigneeUpdateRequest;
import com.example.backend.dto.request.ChecklistCheckUpdateRequest;
import com.example.backend.dto.request.ChecklistItemCreateRequest;
import com.example.backend.dto.response.ChecklistItemCreateResponse;
import com.example.backend.dto.response.ChecklistItemResponse;
import com.example.backend.entity.ChecklistItem;
import com.example.backend.entity.Expedition;
import com.example.backend.entity.User;
import com.example.backend.repository.ChecklistItemRepository;
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
public class ChecklistItemService {

    private final ChecklistItemRepository checklistItemRepository;
    private final ExpeditionRepository expeditionRepository;
    private final ExpeditionMemberRepository expeditionMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChecklistItemCreateResponse createChecklistItem(
            Long userId,
            Long expeditionId,
            ChecklistItemCreateRequest request
    ) {

        // 1. 등록하는 사용자 확인
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

        // 3. 등록자가 해당 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 준비물을 등록할 수 있습니다."
            );
        }

        // 4. 담당자 처리
        User assignedUser = null;

        if (request.assignedUserId() != null) {

            assignedUser = userRepository
                    .findById(request.assignedUserId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "존재하지 않는 담당자입니다."
                    ));

            boolean isAssignedUserMember =
                    expeditionMemberRepository
                            .existsByExpedition_ExpeditionIdAndUser_UserId(
                                    expeditionId,
                                    assignedUser.getUserId()
                            );

            if (!isAssignedUserMember) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "원정 참가자만 준비물 담당자로 지정할 수 있습니다."
                );
            }
        }

        // 5. 준비물 생성
        ChecklistItem checklistItem =
                new ChecklistItem(
                        expedition,
                        user,
                        assignedUser,
                        request.name()
                );

        ChecklistItem saved =
                checklistItemRepository.save(checklistItem);

        // 6. 응답
        return new ChecklistItemCreateResponse(
                saved.getChecklistItemId(),
                expedition.getExpeditionId(),
                saved.getName(),
                saved.getIsChecked(),

                assignedUser != null
                        ? assignedUser.getUserId()
                        : null,

                assignedUser != null
                        ? assignedUser.getNickname()
                        : null,

                user.getUserId(),
                user.getNickname()
        );
    }

    @Transactional(readOnly = true)
    public List<ChecklistItemResponse> getChecklistItems(
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
                    "해당 원정의 참가자만 준비물을 조회할 수 있습니다."
            );
        }

        // 3. 준비물 목록 조회
        List<ChecklistItem> items =
                checklistItemRepository
                        .findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
                                expeditionId
                        );

        // 4. DTO 변환
        return items.stream()
                .map(item -> new ChecklistItemResponse(
                        item.getChecklistItemId(),
                        item.getName(),
                        item.getIsChecked(),
                        item.getCheckedAt(),

                        item.getAssignedUser() != null
                                ? item.getAssignedUser().getUserId()
                                : null,

                        item.getAssignedUser() != null
                                ? item.getAssignedUser().getNickname()
                                : null,

                        item.getCreatedBy().getUserId(),
                        item.getCreatedBy().getNickname()
                ))
                .toList();
    }

    @Transactional
    public ChecklistItemResponse updateAssignee(
            Long userId,
            Long expeditionId,
            Long checklistItemId,
            ChecklistAssigneeUpdateRequest request
    ) {

        // 1. 요청한 사용자가 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 담당자를 변경할 수 있습니다."
            );
        }

        // 2. 해당 원정의 준비물인지 확인
        ChecklistItem item =
                checklistItemRepository
                        .findByChecklistItemIdAndExpedition_ExpeditionId(
                                checklistItemId,
                                expeditionId
                        )
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "존재하지 않는 준비물입니다."
                        ));

        // 3. 담당자 변경
        User assignedUser = null;

        if (request.assignedUserId() != null) {

            assignedUser = userRepository
                    .findById(request.assignedUserId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "존재하지 않는 사용자입니다."
                    ));

            boolean isAssignedMember =
                    expeditionMemberRepository
                            .existsByExpedition_ExpeditionIdAndUser_UserId(
                                    expeditionId,
                                    assignedUser.getUserId()
                            );

            if (!isAssignedMember) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "원정 참가자만 담당자로 지정할 수 있습니다."
                );
            }
        }

        item.assignUser(assignedUser);

        return toResponse(item);
    }
    private ChecklistItemResponse toResponse(ChecklistItem item) {

        return new ChecklistItemResponse(
                item.getChecklistItemId(),
                item.getName(),
                item.getIsChecked(),
                item.getCheckedAt(),

                item.getAssignedUser() != null
                        ? item.getAssignedUser().getUserId()
                        : null,

                item.getAssignedUser() != null
                        ? item.getAssignedUser().getNickname()
                        : null,

                item.getCreatedBy().getUserId(),
                item.getCreatedBy().getNickname()
        );
    }
    //체크/체크 해제 기능 추가
    @Transactional
    public ChecklistItemResponse updateChecked(
            Long userId,
            Long expeditionId,
            Long checklistItemId,
            ChecklistCheckUpdateRequest request
    ) {

        // 1. 원정 참가자인지 확인
        boolean isMember =
                expeditionMemberRepository
                        .existsByExpedition_ExpeditionIdAndUser_UserId(
                                expeditionId,
                                userId
                        );

        if (!isMember) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "해당 원정의 참가자만 준비물 상태를 변경할 수 있습니다."
            );
        }

        // 2. 준비물 확인
        ChecklistItem item =
                checklistItemRepository
                        .findByChecklistItemIdAndExpedition_ExpeditionId(
                                checklistItemId,
                                expeditionId
                        )
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "존재하지 않는 준비물입니다."
                        ));

        // 3. 체크 상태 변경
        item.updateChecked(request.isChecked());

        return toResponse(item);
    }
}