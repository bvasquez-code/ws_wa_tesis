package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.TopicRegisterDto;
import com.ccadmin.app.schooldata.model.dto.TopicRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.TopicEntity;
import com.ccadmin.app.schooldata.service.TopicService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam int TopicID) {
        try {
            TopicEntity topic = this.topicService.findById(TopicID);
            return new ResponseEntity<>(new ResponseWsDto(topic), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<TopicEntity> result = this.topicService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam int TopicID) {
        try {
            return new ResponseEntity<>(this.topicService.findDataForm(TopicID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody TopicRegisterDto topicRegister) {
        try {
            TopicRegisterDto saved = this.topicService.save(topicRegister);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody TopicRegisterMassiveDto topicRegisterMassive) {
        try {
            ResponseWsDto response = this.topicService.saveAll(topicRegisterMassive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}