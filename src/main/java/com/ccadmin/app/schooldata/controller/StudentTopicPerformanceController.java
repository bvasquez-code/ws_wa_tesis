package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.StudentTopicPerformanceRegisterDto;
import com.ccadmin.app.schooldata.model.entity.StudentTopicPerformanceEntity;
import com.ccadmin.app.schooldata.service.StudentTopicPerformanceService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/student-topic-performance")
public class StudentTopicPerformanceController {

    @Autowired
    private StudentTopicPerformanceService studentTopicPerformanceService;

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<StudentTopicPerformanceEntity> result = studentTopicPerformanceService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDetailById")
    public ResponseEntity<ResponseWsDto> findDetailById(@RequestParam String StudentID, @RequestParam int TopicID) {
        try {
            return new ResponseEntity<>(studentTopicPerformanceService.findDetailById(StudentID, TopicID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody StudentTopicPerformanceRegisterDto registerDto) {
        try {
            StudentTopicPerformanceRegisterDto saved = studentTopicPerformanceService.save(registerDto);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete")
    public ResponseEntity<ResponseWsDto> delete(@RequestBody StudentTopicPerformanceRegisterDto registerDto) {
        try {
            ResponseWsDto response = studentTopicPerformanceService.delete(registerDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}