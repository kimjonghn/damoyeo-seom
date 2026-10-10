package com.example.backend.service;

import com.example.backend.dto.request.SignupRequest;
import com.example.backend.dto.response.SignupResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        // 1. 이메일 중복 확인
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이미 가입된 이메일입니다."
            );
        }

        // 2. 비밀번호 암호화
        String encodedPassword =
                passwordEncoder.encode(request.password());

        // 3. User 객체 생성
        User user = new User(
                request.email(),
                encodedPassword,
                request.nickname()
        );

        // 4. DB 저장
        User savedUser = userRepository.save(user);

        // 5. 응답
        return new SignupResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getNickname()
        );
    }
}