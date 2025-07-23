package com.ccadmin.app.product.model.entity;

import com.ccadmin.app.pucharse.model.entity.PucharseDetDeliveryEntity;
import com.ccadmin.app.pucharse.model.entity.PucharseDetEntity;
import com.ccadmin.app.sale.model.entity.SaleDetWarehouseEntity;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Getter
@Table( name = "kardex" )
public class KardexEntity extends AuditTableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long kardexID;
    public String OperationCod;
    public String SourceTable;
    public String TypeOperation;
    public String ProductCod;
    public String Variant;
    public String StoreCod;
    public String WarehouseCod;
    public int NumStockBefore;
    public int NumStockMoved;
    public int NumStockAfter;
    public int TypeOperationCod;

    public KardexEntity()
    {

    }

    public KardexEntity(KardexEntity kardexLast, PucharseDetDeliveryEntity pucharseDetDelivery,String StoreCod)
    {
        this.OperationCod = pucharseDetDelivery.PucharseCod;
        this.SourceTable = "pucharse_head";
        this.TypeOperation = "S";
        this.ProductCod = pucharseDetDelivery.ProductCod;
        this.Variant = pucharseDetDelivery.Variant;
        this.StoreCod = StoreCod;
        this.WarehouseCod = pucharseDetDelivery.WarehouseCod;
        this.NumStockBefore = ( kardexLast == null ) ? 0 : kardexLast.NumStockAfter;
        this.NumStockMoved = pucharseDetDelivery.NumUnit;
        this.NumStockAfter = this.NumStockBefore + pucharseDetDelivery.NumUnit;
        this.TypeOperationCod = 2;
    }

    public KardexEntity(KardexEntity kardexLast, SaleDetWarehouseEntity saleDetWarehouse, String StoreCod)
    {
        this.OperationCod = saleDetWarehouse.SaleCod;
        this.SourceTable = "sale_head";
        this.TypeOperation = "R";
        this.ProductCod = saleDetWarehouse.ProductCod;
        this.Variant = saleDetWarehouse.Variant;
        this.StoreCod = StoreCod;
        this.WarehouseCod = saleDetWarehouse.WarehouseCod;
        this.NumStockBefore = kardexLast.NumStockAfter;
        this.NumStockMoved = saleDetWarehouse.NumUnit;
        this.NumStockAfter = this.NumStockBefore - saleDetWarehouse.NumUnit;
        this.TypeOperationCod = 1;
    }

    @Override
    public KardexEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }
}
