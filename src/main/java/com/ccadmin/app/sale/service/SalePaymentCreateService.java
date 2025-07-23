package com.ccadmin.app.sale.service;

import com.ccadmin.app.payment.model.entity.TrxPaymentEntity;
import com.ccadmin.app.payment.shared.TrxPaymentShared;
import com.ccadmin.app.sale.exception.SalePaymentException;
import com.ccadmin.app.sale.model.dto.SalePaymentDto;
import com.ccadmin.app.sale.model.entity.SaleHeadEntity;
import com.ccadmin.app.sale.model.entity.SalePaymentEntity;
import com.ccadmin.app.sale.repository.SaleHeadRepository;
import com.ccadmin.app.sale.repository.SalePaymentRepository;
import com.ccadmin.app.shared.model.myconst.StatusConst;
import com.ccadmin.app.shared.service.SessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SalePaymentCreateService extends SessionService {

    @Autowired
    private SalePaymentRepository salePaymentRepository;
    @Autowired
    private SaleHeadRepository saleHeadRepository;
    @Autowired
    private TrxPaymentShared trxPaymentShared;
    @Autowired
    private SaleCreateService saleCreateService;

    @Transactional
    public SalePaymentEntity save(SalePaymentDto payment) throws Exception {

        TrxPaymentEntity trxPayment = this.trxPaymentShared.findById(payment.TrxPaymentId);
        BigDecimal TotalPayment = this.salePaymentRepository.findTotalPayment(payment.SaleCod);
        SaleHeadEntity saleHead = this.saleHeadRepository.findById(payment.SaleCod).get();

        if(saleHead.SaleStatus.equals(StatusConst.CONFIRMED)){
            throw new SalePaymentException("Sale has already been completed");
        }
        if( TotalPayment.doubleValue() >= saleHead.NumTotalPrice.doubleValue() ) {
            throw new SalePaymentException("Sale is completed payment");
        }

        BigDecimal NumAmountPaid = trxPayment.AmountPaid.multiply(saleHead.NumExchangevalue);
        BigDecimal NumAmountReturned = TotalPayment.add(NumAmountPaid).subtract(saleHead.NumTotalPrice);

        SalePaymentEntity salePayment = new SalePaymentEntity(
                 saleHead
                ,trxPayment
                ,this.salePaymentRepository.countTotalPayment(payment.SaleCod)
                ,NumAmountPaid
                ,NumAmountReturned
        ).build();
        salePayment.addSession(getUserCod());

        salePaymentRepository.save(salePayment);
        if( TotalPayment.add(salePayment.NumAmountPaid).doubleValue() >= saleHead.NumTotalPrice.doubleValue() ){
            this.saleCreateService.confirm(payment.SaleCod,payment.DocumentType,payment.CounterfoilCod);
        }
        return salePayment;
    }
}
