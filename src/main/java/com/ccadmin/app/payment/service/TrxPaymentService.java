package com.ccadmin.app.payment.service;

import com.ccadmin.app.payment.model.entity.TrxPaymentEntity;
import com.ccadmin.app.payment.repository.TrxPaymentRepository;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.service.SessionService;
import com.ccadmin.app.system.model.entity.CurrencyEntity;
import com.ccadmin.app.system.model.entity.PaymentMethodEntity;
import com.ccadmin.app.system.shared.CurrencyShared;
import com.ccadmin.app.system.shared.PaymentMethodShared;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrxPaymentService extends SessionService {

    @Autowired
    private TrxPaymentRepository trxPaymentRepository;
    @Autowired
    private PaymentMethodShared paymentMethodShared;
    @Autowired
    private CurrencyShared currencyShared;

    public TrxPaymentEntity save(TrxPaymentEntity trxPayment){

        trxPayment.addSession(getUserCod(),true);

        CurrencyEntity currencySystem = currencyShared.findCurrencySystem();
        trxPayment.CurrencyCodSys = currencySystem.CurrencyCod;

        return this.trxPaymentRepository.save(trxPayment);
    }

    public TrxPaymentEntity findById(Long TrxPaymentId){
        return this.trxPaymentRepository.findById(TrxPaymentId).get();
    }

    public ResponseWsDto findDataForm(){

        ResponseWsDto rpt = new ResponseWsDto();

        List<PaymentMethodEntity> paymentMethodList = this.paymentMethodShared.findAllActive();
        List<CurrencyEntity> currencyList = this.currencyShared.findAllActive();

        rpt.AddResponseAdditional("paymentMethodList",paymentMethodList);
        rpt.AddResponseAdditional("currencyList",currencyList);

        return rpt;
    }
}
