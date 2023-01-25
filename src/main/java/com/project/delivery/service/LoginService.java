package com.project.delivery.service;

import com.project.delivery.model.dto.JoinRequest;
import com.project.delivery.model.dto.LoginRequest;
import com.project.delivery.model.entity.Member;

public interface LoginService {
    Member join(JoinRequest joinRequest);
    String login(LoginRequest loginRequest);
}
