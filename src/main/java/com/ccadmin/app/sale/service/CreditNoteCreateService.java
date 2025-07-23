package com.ccadmin.app.sale.service;


import com.ccadmin.app.sale.exception.SaleException;
import com.ccadmin.app.sale.model.constants.SaleConstants;
import com.ccadmin.app.sale.model.dto.CreditNoteDetailDto;
import com.ccadmin.app.sale.model.dto.CreditNoteRegisterDto;
import com.ccadmin.app.sale.model.entity.*;
import com.ccadmin.app.sale.repository.*;
import com.ccadmin.app.shared.service.SessionService;
import com.ccadmin.app.system.shared.CounterfoilShared;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CreditNoteCreateService extends SessionService {

    @Autowired
    private CreditNoteHeadRepository creditNoteHeadRepository;
    @Autowired
    private CreditNoteDetRepository creditNoteDetRepository;
    @Autowired
    private SaleHeadRepository saleHeadRepository;
    @Autowired
    private SaleDetRepository saleDetRepository;
    @Autowired
    private SaleDocumentRepository saleDocumentRepository;
    @Autowired
    private CreditNoteDocumentRepository creditNoteDocumentRepository;
    @Autowired
    private CreditNoteSearchService creditNoteSearchService;
    @Autowired
    private CounterfoilShared counterfoilShared;

    public String createCode(){
        String PresaleCod = creditNoteHeadRepository.getCreditNoteCod(getStoreCod());
        return PresaleCod;
    }

    @Transactional
    public CreditNoteDetailDto save(CreditNoteRegisterDto creditNoteRegister) throws SaleException {

        log.info("INI_CREACION_NOTA_CREDITO -->> {}",creditNoteRegister.Headboard.CreditNoteCod);

        this.validateCreditNoteRegisterDto(creditNoteRegister);

        SaleHeadEntity saleHead = this.saleHeadRepository.findById(creditNoteRegister.Headboard.SaleCod).get();

        SaleDocumentEntity saleDocument = this.saleDocumentRepository.findBySaleCod(saleHead.SaleCod);

        creditNoteRegister.DetailList.forEach( product -> {
            product.CreditNoteCod = creditNoteRegister.Headboard.CreditNoteCod;
            product.NumTotalPrice = product.NumUnitPriceSale.multiply(BigDecimal.valueOf(product.NumUnit));
            product.validate().session(getUserCod());
        });

        creditNoteRegister.Headboard.NumTotalPrice = creditNoteRegister.DetailList
                .stream()
                .map( product -> product.NumTotalPrice )
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        creditNoteRegister.Headboard
                .build(saleHead, SaleConstants.PENDING)
                .validate()
                .session(getUserCod());

        String GroupDocument = (saleDocument.DocumentCod.startsWith("B")) ? "B" : "F";


        CreditNoteDocumentEntity creditNoteDocument = (creditNoteRegister.Document == null || creditNoteRegister.Document.DocumentCod.isEmpty()) ?
                this.counterfoilShared.generateDocumentCreditNote(getStoreCod(),"07",creditNoteRegister.Headboard.CreditNoteCod,GroupDocument)
                : creditNoteRegister.Document;

        log.info("DOCUMENTO_NOTA_CREDITO -->> {}",creditNoteDocument.DocumentCod);

        this.creditNoteDetRepository.updateStatusAll(creditNoteRegister.Headboard.CreditNoteCod,"I");
        this.creditNoteHeadRepository.save(creditNoteRegister.Headboard);
        this.creditNoteDetRepository.saveAll(creditNoteRegister.DetailList);
        this.creditNoteDocumentRepository.save(creditNoteDocument);

        log.info("FIN_CREACION_NOTA_CREDITO -->> {}",creditNoteRegister.Headboard.CreditNoteCod);

        return this.creditNoteSearchService.findById(creditNoteRegister.Headboard.CreditNoteCod);
    }

    @Transactional
    public CreditNoteDetailDto confirm(CreditNoteRegisterDto creditNoteRegister) throws SaleException {

        if(creditNoteRegister.Headboard.CreditNoteStatus.equals(SaleConstants.CONFIRMED)){
            throw new SaleException("Nota de crédito ya fue confirmada");
        }

        CreditNoteHeadEntity creditNoteHead = this.creditNoteHeadRepository.findById(creditNoteRegister.Headboard.CreditNoteCod).get();
        creditNoteHead.CreditNoteStatus = SaleConstants.CONFIRMED;

        this.creditNoteHeadRepository.save(creditNoteHead);

        return this.creditNoteSearchService.findById(creditNoteRegister.Headboard.CreditNoteCod);
    }

    private void validateCreditNoteRegisterDto(CreditNoteRegisterDto creditNoteRegister) throws SaleException {
        if(creditNoteRegister.Headboard == null){
            throw new SaleException("No existe cabecera en la nota de crédito");
        }
        if(creditNoteRegister.DetailList == null || creditNoteRegister.DetailList.isEmpty()){
            throw new SaleException("El detalle de la nota de crédito esta vació");
        }
        if(creditNoteRegister.Headboard.CreditNoteStatus.equals(SaleConstants.CONFIRMED)){
            throw new SaleException("Nota de crédito ya fue confirmada no se puede editar");
        }
        List<SaleDetEntity> saleDetList = this.saleDetRepository.findBySaleCod(creditNoteRegister.Headboard.SaleCod);
        for(var product : creditNoteRegister.DetailList){
            if(saleDetList.stream().noneMatch(e -> e.ProductCod.equals(product.ProductCod))){
                throw new SaleException(" producto no existe en la compra de origen  "+product.ProductCod);
            }
        }

        if(creditNoteRegister.Document == null || creditNoteRegister.Document.DocumentCod.isEmpty()){
            CreditNoteHeadEntity creditNoteHead = this.creditNoteHeadRepository.findBySaleCod(creditNoteRegister.Headboard.SaleCod);
            if(creditNoteHead != null){
                throw new SaleException("Venta ya tiene asociada una nota de crédito");
            }
        }
    }
}
