package com.project.delivery.repository;

import com.project.delivery.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByMemberIdAndCreateDateBetween(String id, LocalDateTime start, LocalDateTime end);
    Delivery findBySeqNo(Long seqNo);
}
