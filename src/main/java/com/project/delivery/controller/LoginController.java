package com.project.delivery.controller;

import com.project.delivery.config.JwtTokenProvider;
import com.project.delivery.model.dto.JoinRequest;
import com.project.delivery.model.dto.LoginRequest;
import com.project.delivery.model.dto.LoginResponse;
import com.project.delivery.service.LoginService;
import io.swagger.annotations.ApiOperation;
import com.project.delivery.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping(value = "/join")
    public ApiResponse<?> join(@RequestBody @Valid JoinRequest joinRequest) {
        loginService.join(joinRequest);

        return ApiResponse.createSuccess("회원가입이 완료되었습니다.");
    }

    @ApiOperation(value = "로그인", notes = "로그인")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        String token = loginService.login(loginRequest);

        return ApiResponse.createSuccess(new LoginResponse(token), "로그인이 완료되었습니다.");
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃")
    @GetMapping("/userLogout")
    public ApiResponse<?> logout(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (jwtTokenProvider.validateToken(token)) {
            Long expiration = jwtTokenProvider.getExpiration(token);
            redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
        }

        return ApiResponse.createSuccess("로그아웃이 완료되었습니다.");
    }
}