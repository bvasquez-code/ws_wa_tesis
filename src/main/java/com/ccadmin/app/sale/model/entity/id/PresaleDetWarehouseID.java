package com.ccadmin.app.sale.model.entity.id;

import java.io.Serializable;

public class PresaleDetWarehouseID implements Serializable {

    public String PresaleCod;
    public String ProductCod;
    public String Variant;
    public String WarehouseCod;

    public PresaleDetWarehouseID()
    {

    }

    public PresaleDetWarehouseID(String presaleCod, String productCod, String variant, String warehouseCod) {
        PresaleCod = presaleCod;
        ProductCod = productCod;
        Variant = variant;
        WarehouseCod = warehouseCod;
    }
}
