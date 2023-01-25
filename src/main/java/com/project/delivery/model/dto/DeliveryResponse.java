package com.project.delivery.model.dto;

import com.project.delivery.model.entity.Member;
import lombok.Data;

@Data
public class DeliveryResponse {
    private Member member;

    private String address;
}