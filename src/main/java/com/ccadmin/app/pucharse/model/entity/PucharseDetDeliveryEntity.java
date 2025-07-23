package com.ccadmin.app.pucharse.model.entity;

import com.ccadmin.app.pucharse.model.entity.id.PucharseDetDeliveryId;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table( name = "pucharse_det_delivery")
@IdClass( PucharseDetDeliveryId.class )
public class PucharseDetDeliveryEntity extends AuditTableEntity implements Serializable {

    @Id
    public String PucharseCod;
    @Id
    public String ProductCod;
    @Id
    public String Variant;
    @Id
    public String WarehouseCod;
    public int NumUnit;
}
