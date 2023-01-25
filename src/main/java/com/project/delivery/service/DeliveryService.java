package com.project.delivery.service;

import com.project.delivery.model.dto.DeliveryAddRequest;
import com.project.delivery.model.dto.DeliveryUpdateRequest;
import com.project.delivery.model.entity.Delivery;
import com.project.delivery.model.enums.Status;

import java.util.List;

public interface DeliveryService {
    List<Delivery> getDelivery(String id, int period);
    void addDelivery(DeliveryAddRequest deliveryAddRequest);
    void addressDelivery(Long deliverySeqno, DeliveryUpdateRequest deliveryUpdateRequest);
    void statusDelivery(Long deliverySeqno, Status status);
}
