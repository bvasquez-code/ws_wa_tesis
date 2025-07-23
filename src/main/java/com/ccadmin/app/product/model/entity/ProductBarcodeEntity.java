package com.ccadmin.app.product.model.entity;

import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "product_barcode")
public class ProductBarcodeEntity extends AuditTableEntity implements Serializable {

    @Id
    public String BarCode;
    public String ProductCod;

}
