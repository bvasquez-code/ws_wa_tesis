package com.ccadmin.app.payment.repository;

import com.ccadmin.app.payment.model.entity.TrxPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrxPaymentRepository extends JpaRepository<TrxPaymentEntity, Long> {
}
