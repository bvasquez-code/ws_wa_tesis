package com.ccadmin.app.sale.controller;

import com.ccadmin.app.sale.model.dto.SalePaymentDto;
import com.ccadmin.app.sale.service.SalePaymentCreateService;
import com.ccadmin.app.sale.service.SaleSearchService;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sale")
public class SaleController {

    public static Logger log = LogManager.getLogger(SaleController.class);
    @Autowired
    private SalePaymentCreateService salePaymentCreateService;
    @Autowired
    private SaleSearchService saleSearchService;

    @PostMapping("addPayment")
    public ResponseEntity<ResponseWsDto> addPayment(@RequestBody SalePaymentDto salePayment)
    {
        try{
            return new ResponseEntity<ResponseWsDto>(
                    new ResponseWsDto(this.salePaymentCreateService.save(salePayment))
                    , HttpStatus.OK
            );
        }
        catch (Exception ex)
        {
            log.error("Error :{}",ex.getMessage(), ex);
            return new ResponseEntity<ResponseWsDto>(new ResponseWsDto(ex),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam String SaleCod)
    {
        try{
            return new ResponseEntity<ResponseWsDto>(
                    this.saleSearchService.findDataForm(SaleCod)
                    ,HttpStatus.OK
            );
        }
        catch (Exception ex)
        {
            return new ResponseEntity<ResponseWsDto>(new ResponseWsDto(ex),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query,int Page,String StoreCod)
    {
        try{
            return new ResponseEntity<ResponseWsDto>(
                    new ResponseWsDto(this.saleSearchService.findAll(Query,Page,StoreCod))
                    ,HttpStatus.OK
            );
        }
        catch (Exception ex)
        {
            return new ResponseEntity<ResponseWsDto>(new ResponseWsDto(ex),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findByDocumentCod")
    public ResponseEntity<ResponseWsDto> findByDocumentCod(@RequestParam String DocumentCod)
    {
        try{
            return new ResponseEntity<ResponseWsDto>(
                    new ResponseWsDto().okResponse(this.saleSearchService.findByDocumentCod(DocumentCod))
                    ,HttpStatus.OK
            );
        }
        catch (Exception ex)
        {
            return new ResponseEntity<ResponseWsDto>(new ResponseWsDto(ex),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam String SaleCod)
    {
        try{
            return new ResponseEntity<ResponseWsDto>(
                    new ResponseWsDto().okResponse(this.saleSearchService.findById(SaleCod))
                    ,HttpStatus.OK
            );
        }
        catch (Exception ex)
        {
            return new ResponseEntity<ResponseWsDto>(new ResponseWsDto(ex),HttpStatus.BAD_REQUEST);
        }
    }

}
