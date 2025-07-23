package com.ccadmin.app.sale.model.entity;

import com.ccadmin.app.sale.exception.SaleBuildException;
import com.ccadmin.app.sale.model.entity.id.SaleDetWarehouseID;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table( name = "sale_det_warehouse")
@IdClass( SaleDetWarehouseID.class )
public class SaleDetWarehouseEntity extends AuditTableEntity implements Serializable {
    @Id
    public String SaleCod;
    @Id
    public String ProductCod;
    @Id
    public String Variant;
    @Id
    public String WarehouseCod;
    public int NumUnit;

    public SaleDetWarehouseEntity()
    {

    }

    public SaleDetWarehouseEntity(PresaleDetWarehouseEntity detWarehouse,String SaleCod)
    {
        this.SaleCod = SaleCod;
        this.ProductCod = detWarehouse.ProductCod;
        this.Variant = detWarehouse.Variant;
        this.WarehouseCod = detWarehouse.WarehouseCod;
        this.NumUnit = detWarehouse.NumUnit;
    }

    public SaleDetWarehouseEntity build(PresaleDetWarehouseEntity detWarehouse,String SaleCod)
    {
        this.SaleCod = SaleCod;
        this.ProductCod = detWarehouse.ProductCod;
        this.Variant = detWarehouse.Variant;
        this.WarehouseCod = detWarehouse.WarehouseCod;
        this.NumUnit = detWarehouse.NumUnit;
        return this;
    }
    public SaleDetWarehouseEntity validate() throws SaleBuildException {
        if(this.SaleCod==null || this.SaleCod.isEmpty()){
            throw new SaleBuildException("Código de venta esta vacío");
        }
        if(this.NumUnit <= 0){
            throw new SaleBuildException("Número de unidades del productos debe ser mayor a cero");
        }
        return this;
    }
    @Override
    public SaleDetWarehouseEntity session(String userCod){
        this.addSession(userCod);
        return this;
    }
}
