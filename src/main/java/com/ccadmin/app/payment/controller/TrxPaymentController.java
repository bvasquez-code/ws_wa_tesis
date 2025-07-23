package com.ccadmin.app.payment.controller;

import com.ccadmin.app.payment.model.entity.TrxPaymentEntity;
import com.ccadmin.app.payment.service.TrxPaymentService;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/TrxPayment")
@Slf4j
public class TrxPaymentController {

    @Autowired
    private TrxPaymentService trxPaymentService;

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody TrxPaymentEntity trxPayment)
    {
        try{
            return new ResponseEntity<ResponseWsDto>(
                    new ResponseWsDto(this.trxPaymentService.save(trxPayment))
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
    public ResponseEntity<ResponseWsDto> findDataForm(){
        try{
            return new ResponseEntity<ResponseWsDto>(
                    this.trxPaymentService.findDataForm()
                    , HttpStatus.OK
            );
        }
        catch (Exception ex)
        {
            log.error("Error :"+ex.getMessage(), ex);
            return new ResponseEntity<ResponseWsDto>(new ResponseWsDto(ex),HttpStatus.BAD_REQUEST);
        }
    }
}
