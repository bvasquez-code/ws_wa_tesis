package com.ccadmin.app.sale.model.entity;

import com.ccadmin.app.sale.model.entity.id.SaleDocumentID;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table( name = "sale_document" )
@IdClass(SaleDocumentID.class)
public class SaleDocumentEntity extends AuditTableEntity implements Serializable {

    @Id
    public String DocumentCod;
    @Id
    public String CounterfoilCod;
    public String SaleCod;

}
