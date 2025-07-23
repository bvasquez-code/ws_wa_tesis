package com.ccadmin.app.sale.model.entity;

import com.ccadmin.app.sale.exception.PresaleBuildException;
import com.ccadmin.app.sale.model.entity.id.PresaleDetWarehouseID;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import com.ccadmin.app.store.model.entity.WarehouseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table( name = "presale_det_warehouse" )
@IdClass(PresaleDetWarehouseID.class)
public class PresaleDetWarehouseEntity extends AuditTableEntity implements Serializable {

    @Id
    public String PresaleCod;
    @Id
    public String ProductCod;
    @Id
    public String Variant;
    @Id
    public String WarehouseCod;
    public int NumUnit;

    public PresaleDetWarehouseEntity(){

    }

    public PresaleDetWarehouseEntity build(PresaleDetEntity presaleDet,WarehouseEntity warehouseDefault){
        this.PresaleCod = presaleDet.PresaleCod;
        this.ProductCod = presaleDet.ProductCod;
        this.Variant = presaleDet.Variant;
        this.NumUnit = presaleDet.NumUnit;
        this.WarehouseCod = warehouseDefault.WarehouseCod;
        return this;
    }

    public PresaleDetWarehouseEntity validate() throws PresaleBuildException {
        if(this.PresaleCod==null || this.PresaleCod.isEmpty()){
            throw new PresaleBuildException("Código de preventa esta vació.");
        }
        if(this.NumUnit == 0){
            throw new PresaleBuildException("Número de productos no puede ser cero.");
        }
        return this;
    }

    @Override
    public PresaleDetWarehouseEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }
}
