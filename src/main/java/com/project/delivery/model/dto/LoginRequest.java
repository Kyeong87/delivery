package com.project.delivery.model.dto;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {

    @ApiModelProperty("아이디")
    private String id;

    @ApiModelProperty("패스워드")
    @NotNull
    private String password;

    public LoginRequest(){}
}
