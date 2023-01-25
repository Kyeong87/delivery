package com.project.delivery.controller;

import com.project.delivery.model.dto.DeliveryAddRequest;
import com.project.delivery.model.dto.DeliveryUpdateRequest;
import com.project.delivery.model.entity.Delivery;
import com.project.delivery.model.enums.Status;
import com.project.delivery.service.DeliveryService;
import com.project.delivery.util.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DeliveryController {
    private final DeliveryService deliveryService;

    @ApiOperation(value = "배달 조회", notes = "배달 조회")
    @GetMapping ("/delivery")
    public List<Delivery> delivery(
            @RequestParam(value = "period", defaultValue = "0")
            @Min(value = 0, message = "0 보다 큰 정수여야 합니다.")
            @Max(value = 3, message = "최대 3일까지만 조회 가능합니다.")
            int period
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return deliveryService.getDelivery(userDetails.getUsername(), period);
    }

    @ApiOperation(value = "배달 등록", notes = "배달 등록")
    @PostMapping("/delivery")
    public ApiResponse<?> addDelivery(@RequestBody @Valid DeliveryAddRequest deliveryAddRequest) {
        deliveryService.addDelivery(deliveryAddRequest);

        return ApiResponse.createSuccess("배달 등록이 완료되었습니다.");
    }

    @ApiOperation(value = "배달 도착지 주소 변경", notes = "배달 도착지 주소 변경")
    @PatchMapping("/delivery/address/{delivery_seqno}")
    public ApiResponse<?> addressDelivery(
            @PathVariable(name = "delivery_seqno")
            long deliverySeqno,
            @RequestBody
            @Valid
            DeliveryUpdateRequest deliveryUpdateRequest) {
        deliveryService.addressDelivery(deliverySeqno, deliveryUpdateRequest);

        return ApiResponse.createSuccess("도착지 주소 변경이 완료되었습니다.");
    }

    @ApiOperation(value = "배달 상태 변경", notes = "배달 상태 변경")
    @PatchMapping("/delivery/status/{delivery_seqno}")
    public ApiResponse<?> statusDelivery(
            @PathVariable(name = "delivery_seqno")
            long deliverySeqno,
            @RequestBody
            @Valid
            Status status) {
        deliveryService.statusDelivery(deliverySeqno, status);

        return ApiResponse.createSuccess("상태 변경이 완료되었습니다.");
    }

}