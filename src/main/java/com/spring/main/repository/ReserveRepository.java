package com.spring.main.repository;

import com.spring.main.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<ReservationEntity, Long> {

}
