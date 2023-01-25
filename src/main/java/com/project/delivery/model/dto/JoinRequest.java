package com.project.delivery.model.dto;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JoinRequest {
    @ApiModelProperty("아이디")
    @NotNull
    private String id;

    @ApiModelProperty("패스워드")
    @NotNull
    private String password;

    @ApiModelProperty("이름")
    @NotNull
    private String name;

    @ApiModelProperty("휴대폰")
    @NotNull
    private String phone;

    public JoinRequest() {

    }
}
