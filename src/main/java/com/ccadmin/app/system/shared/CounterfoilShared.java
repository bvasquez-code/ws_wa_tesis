package com.ccadmin.app.system.shared;

import com.ccadmin.app.sale.model.entity.CreditNoteDocumentEntity;
import com.ccadmin.app.sale.model.entity.SaleDocumentEntity;
import com.ccadmin.app.system.service.CounterfoilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterfoilShared {

    @Autowired
    private CounterfoilService counterfoilService;

    public SaleDocumentEntity generateDocumentSale(String StoreCod, String DocumentType,String SaleCod)
    {
        return this.counterfoilService.generateDocumentSale(StoreCod,DocumentType,SaleCod);
    }

    public CreditNoteDocumentEntity generateDocumentCreditNote(String StoreCod, String DocumentType, String CreditNoteCod, String GroupDocument)
    {
        return this.counterfoilService.generateDocumentCreditNote(StoreCod,DocumentType,CreditNoteCod,GroupDocument);
    }


}
