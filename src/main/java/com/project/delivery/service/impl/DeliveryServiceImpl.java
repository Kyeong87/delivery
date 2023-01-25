package com.project.delivery.service.impl;

import com.project.delivery.config.exception.ApiException;
import com.project.delivery.model.dto.DeliveryAddRequest;
import com.project.delivery.model.dto.DeliveryUpdateRequest;
import com.project.delivery.model.entity.Delivery;
import com.project.delivery.model.entity.Member;
import com.project.delivery.model.enums.Status;
import com.project.delivery.repository.DeliveryRepository;
import com.project.delivery.repository.LoginRepository;
import com.project.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final LoginRepository loginRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository,
                               LoginRepository loginRepository) {
        this.deliveryRepository = deliveryRepository;
        this.loginRepository = loginRepository;
    }

    @Override
    public List<Delivery> getDelivery(String id, int period) {
        if(period > 3) throw new ApiException(HttpStatus.BAD_REQUEST, "최대 3일 까지만 조회 가능합니다.");
        else if(period < 0) throw new ApiException(HttpStatus.BAD_REQUEST, "0 보다 큰 정수여야 합니다.");

        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(period), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

        List<Delivery> findMember = deliveryRepository.findByMemberIdAndCreateDateBetween(id,startDatetime, endDatetime);

        return findMember;
    }

    @Override
    @Transactional
    public void addDelivery(DeliveryAddRequest deliveryAddRequest) {
        Member findMember =
                loginRepository.findById(deliveryAddRequest.getMemberId());
        if(Objects.isNull(findMember)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "등록된 회원이 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Delivery delivery = new Delivery();
        delivery.setMember(findMember);
        delivery.setStatus(Status.WAIT);
        delivery.setDepartAddress(deliveryAddRequest.getDepartAddress());
        delivery.setArrivalAddress(deliveryAddRequest.getArrivalAddress());
        delivery.setCreateDate(now);

        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void addressDelivery(Long deliverySeqno, DeliveryUpdateRequest deliveryUpdateRequest) {
        Delivery delivery =
                deliveryRepository.findBySeqNo(deliverySeqno);

        if(delivery.getStatus() != Status.WAIT) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "이미 배달이 출발하여 도착지 주소 변경이 불가능합니다.");
        }

        delivery.setArrivalAddress(deliveryUpdateRequest.getArrivalAddress());

        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void statusDelivery(Long deliverySeqno, Status status) {
        Delivery delivery = deliveryRepository.findBySeqNo(deliverySeqno);
        if (Objects.isNull(delivery)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "변경 가능한 배달이 없습니다.");
        }
        delivery.setStatus(status);

        deliveryRepository.save(delivery);
    }
}
