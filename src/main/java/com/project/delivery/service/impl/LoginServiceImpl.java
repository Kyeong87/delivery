package com.project.delivery.service.impl;

import com.project.delivery.config.JwtTokenProvider;
import com.project.delivery.config.exception.ApiException;
import com.project.delivery.model.dto.JoinRequest;
import com.project.delivery.model.dto.LoginRequest;
import com.project.delivery.model.dto.TokenResponse;
import com.project.delivery.model.entity.Member;
import com.project.delivery.repository.LoginRepository;
import com.project.delivery.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    // 영어 대문자, 영어 소문자, 숫자, 특수문자 중 3종류 이상으로 12자리 이상의 문자열로 생성해야 합니다.
    private static final String COMPLEX_PASSWORD_REGEX =
            "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|" +
            "(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|" +
            "(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|" +
            "(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))|" +
            "[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()\\|\\[\\]\\-\\$\\^\\@\\/]" +
            "{12,}$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(COMPLEX_PASSWORD_REGEX);

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate template;

    @Autowired
    public LoginServiceImpl(LoginRepository loginRepository,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider,
                            StringRedisTemplate template) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.template = template;
    }

    @Override
    @Transactional
    public Member join(JoinRequest joinRequest) {

        Member findMember = loginRepository.findById(joinRequest.getId());
        if (!Objects.isNull(findMember)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "사용중인 아이디 있습니다.");
        }

        Matcher passMatcher = PASSWORD_PATTERN.matcher(joinRequest.getPassword());
        if(!passMatcher.matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "비밀번호는 영어 대문자, 영어 소문자, 숫자, 특수문자 중 3종류 이상으로 12자리 이상의 문자열로 생성해야 합니다.");
        }

        Member member = new Member();
        member.setId(joinRequest.getId());
        member.setName(joinRequest.getName());
        member.setPhone(joinRequest.getPhone());
        member.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
        member.setRoles(Collections.singletonList("ROLE_USER"));

        return loginRepository.save(member);
    }

    @Override
    @Transactional
    public String login(LoginRequest loginRequest) {

        Member findMember = loginRepository.findById(loginRequest.getId());
        if (Objects.isNull(findMember)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "가입되지 않은 아이디 입니다.");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), findMember.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        TokenResponse tokenData = jwtTokenProvider.createToken(findMember.getId(), findMember.getRoles());

        template.opsForValue().set("RT:"+loginRequest.getId(),tokenData.getAccessToken(), tokenData.getTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return tokenData.getAccessToken();
    }

}
