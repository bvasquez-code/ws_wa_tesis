package com.ccadmin.app.payment.model.entity;

import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "trx_payments")
public class TrxPaymentEntity extends AuditTableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long TrxPaymentId;
    public String PaymentMethodCod;
    public String PaymentPlatform;
    public String CardNumber;
    public String CardHolderName;
    public Date CardExpirationDate;
    public String CardCVV;
    public String TransactionId;
    public String PaymentStatus;
    public String CurrencyCod;
    public String CurrencyCodSys;
    public BigDecimal NumExchangevalue;
    public BigDecimal AmountPaid;
    public BigDecimal AmountReturned;
}
