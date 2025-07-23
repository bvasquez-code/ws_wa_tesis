package com.ccadmin.app.system.service;

import com.ccadmin.app.sale.model.entity.CreditNoteDocumentEntity;
import com.ccadmin.app.sale.model.entity.SaleDocumentEntity;
import com.ccadmin.app.shared.service.SessionService;
import com.ccadmin.app.system.model.entity.CounterfoilEntity;
import com.ccadmin.app.system.repository.CounterfoilRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterfoilService extends SessionService {

    @Autowired
    private CounterfoilRepository counterfoilRepository;

    public SaleDocumentEntity generateDocumentSale(String StoreCod, String DocumentType,String SaleCod)
    {
        SaleDocumentEntity saleDocument = new SaleDocumentEntity();
        CounterfoilEntity counterfoil = this.counterfoilRepository.findByStoreDefault(DocumentType,StoreCod);

        counterfoil.Correlative = counterfoil.Correlative + 1;
        this.counterfoilRepository.save(counterfoil);

        int Correlative = 1000000 + counterfoil.Correlative;

        saleDocument.DocumentCod = counterfoil.Series+"-"+String.valueOf(Correlative).substring(1, 7);
        saleDocument.CounterfoilCod = counterfoil.CounterfoilCod;
        saleDocument.SaleCod = SaleCod;
        saleDocument.addSession(getUserCod());
        return saleDocument;
    }

    public CreditNoteDocumentEntity generateDocumentCreditNote(String StoreCod, String DocumentType, String CreditNoteCod, String GroupDocument)
    {
        CreditNoteDocumentEntity creditNoteDocument = new CreditNoteDocumentEntity();
        CounterfoilEntity counterfoil = this.counterfoilRepository.findByStoreDefault(DocumentType,StoreCod,GroupDocument);

        counterfoil.Correlative = counterfoil.Correlative + 1;
        this.counterfoilRepository.save(counterfoil);

        int Correlative = 1000000 + counterfoil.Correlative;

        creditNoteDocument.DocumentCod = counterfoil.Series+"-"+String.valueOf(Correlative).substring(1, 7);
        creditNoteDocument.CounterfoilCod = counterfoil.CounterfoilCod;
        creditNoteDocument.CreditNoteCod = CreditNoteCod;
        creditNoteDocument.addSession(getUserCod());
        return creditNoteDocument;
    }
}
