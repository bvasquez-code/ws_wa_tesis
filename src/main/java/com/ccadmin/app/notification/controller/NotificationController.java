package com.ccadmin.app.notification.controller;

import com.ccadmin.app.notification.model.dto.EmailSendDto;
import com.ccadmin.app.notification.service.EmailSendService;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

    @Autowired
    private EmailSendService emailSendService;

    @PostMapping("/sendEmail")
    public ResponseEntity<ResponseWsDto> sendEmail(@RequestBody EmailSendDto dto) {
        emailSendService.send(dto);
        return ResponseEntity.ok(new ResponseWsDto().okResponse("Correo enviado correctamente"));
    }
}