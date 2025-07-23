package com.ccadmin.app.system.model.entity;

import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table( name = "payment_method")
public class PaymentMethodEntity extends AuditTableEntity implements Serializable {

    @Id
    public String PaymentMethodCod;
    public String Name;
    public String Description;
    public String PaymentMethodType;

}
