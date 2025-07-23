package com.ccadmin.app.system.repository;

import com.ccadmin.app.system.model.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity,String> {

    @Query( value = """
            select pm.* from payment_method pm where pm.Status = 'A'
            """, nativeQuery = true)
    public List<PaymentMethodEntity> findAllActive();
}
