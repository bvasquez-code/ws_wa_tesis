package com.ccadmin.app.sale.model.entity.id;

import java.io.Serializable;

public class CreditNoteDetID implements Serializable {

    public String CreditNoteCod;
    public String ProductCod;
    public String Variant;

    public CreditNoteDetID(){

    }

    public CreditNoteDetID(String creditNoteCod, String productCod, String variant) {
        CreditNoteCod = creditNoteCod;
        ProductCod = productCod;
        Variant = variant;
    }
}
