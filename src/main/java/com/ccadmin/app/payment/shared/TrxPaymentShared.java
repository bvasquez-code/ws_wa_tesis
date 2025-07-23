package com.ccadmin.app.payment.shared;

import com.ccadmin.app.payment.model.entity.TrxPaymentEntity;
import com.ccadmin.app.payment.service.TrxPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrxPaymentShared {
    @Autowired
    private TrxPaymentService trxPaymentService;

    public TrxPaymentEntity save(TrxPaymentEntity trxPayment) {
        return this.trxPaymentService.save(trxPayment);
    }

    public TrxPaymentEntity findById(Long TrxPaymentId){
        return this.trxPaymentService.findById(TrxPaymentId);
    }
}
