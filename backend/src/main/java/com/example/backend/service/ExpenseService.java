package com.example.backend.service;

import com.example.backend.dto.request.ExpenseCreateRequest;
import com.example.backend.dto.response.ExpenseCreateResponse;
import com.example.backend.dto.response.ExpenseResponse;
import com.example.backend.dto.response.ExpenseSummaryResponse;
import com.example.backend.entity.Expedition;
import com.example.backend.entity.Expense;
import com.example.backend.entity.User;
import com.example.backend.repository.ExpeditionMemberRepository;
import com.example.backend.repository.ExpeditionRepository;
import com.example.backend.repository.ExpenseRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
//비용계산
    private final ExpenseRepository expenseRepository;
    private final ExpeditionRepository expeditionRepository;
    private final ExpeditionMemberRepository expeditionMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public ExpenseCreateResponse createExpense(
            Long userId,
            Long expeditionId,
            ExpenseCreateRequest request
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
                    "해당 원정의 참가자만 비용을 등록할 수 있습니다."
            );
        }

        // 4. 비용 생성
        Expense expense = new Expense(
                expedition,
                user,
                request.name(),
                request.amount(),
                request.calculationType(),
                request.quantity()
        );

        Expense savedExpense =
                expenseRepository.save(expense);

        // 5. 응답
        return new ExpenseCreateResponse(
                savedExpense.getExpenseId(),
                expedition.getExpeditionId(),
                savedExpense.getName(),
                savedExpense.getAmount(),
                savedExpense.getCalculationType(),
                savedExpense.getQuantity(),
                savedExpense.getTotalAmount(),
                user.getUserId(),
                user.getNickname()
        );
    }

    //목록/계산 기능 추가
    @Transactional(readOnly = true)
    public ExpenseSummaryResponse getExpenses(
            Long userId,
            Long expeditionId
    ) {

        // 1. 원정 존재 확인
        expeditionRepository.findById(expeditionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 원정입니다."
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
                    "해당 원정의 참가자만 비용을 조회할 수 있습니다."
            );
        }

        // 3. 비용 목록 조회
        List<Expense> expenses =
                expenseRepository
                        .findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
                                expeditionId
                        );

        // 4. 응답 DTO 변환
        List<ExpenseResponse> expenseResponses =
                expenses.stream()
                        .map(expense -> new ExpenseResponse(
                                expense.getExpenseId(),
                                expense.getName(),
                                expense.getAmount(),
                                expense.getCalculationType(),
                                expense.getQuantity(),
                                expense.getTotalAmount(),
                                expense.getCreatedBy().getUserId(),
                                expense.getCreatedBy().getNickname()
                        ))
                        .toList();

        // 5. 전체 비용 계산
        BigDecimal totalAmount =
                expenses.stream()
                        .map(Expense::getTotalAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // 6. 현재 실제 참가 인원 조회
        long participantCount =
                expeditionMemberRepository
                        .countByExpedition_ExpeditionId(
                                expeditionId
                        );

        // 7. 1인당 예상 비용 계산
        BigDecimal perPersonAmount = BigDecimal.ZERO;

        if (participantCount > 0) {
            perPersonAmount =
                    totalAmount.divide(
                            BigDecimal.valueOf(participantCount),
                            0,
                            RoundingMode.HALF_UP
                    );
        }

        return new ExpenseSummaryResponse(
                expeditionId,
                expenseResponses,
                totalAmount,
                participantCount,
                perPersonAmount
        );
    }
}