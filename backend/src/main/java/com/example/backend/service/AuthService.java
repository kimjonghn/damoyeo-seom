package com.example.backend.service;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.SignupRequest;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.dto.response.SignupResponse;
import com.example.backend.entity.User;
import com.example.backend.jwt.JwtProvider;
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
    private final JwtProvider jwtProvider;

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
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "가입되지 않은 이메일입니다."
                ));

        if (user.getPasswordHash() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "일반 로그인을 사용할 수 없는 계정입니다."
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "비밀번호가 일치하지 않습니다."
            );
        }

        // JWT 생성
        String accessToken = jwtProvider.createAccessToken(
                user.getUserId(),
                user.getEmail()
        );

        return new LoginResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                accessToken
        );
    }
}