package com.project.delivery.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeliveryUpdateRequest {
    @ApiModelProperty("회원 아이디")
    private String memberId;

    @ApiModelProperty("도착 주소")
    private String arrivalAddress;

}
