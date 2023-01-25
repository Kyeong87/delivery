package com.project.delivery.config;

import com.project.delivery.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails member = loginRepository.findById(username);
        if (member.getUsername() == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return member;
    }
}
