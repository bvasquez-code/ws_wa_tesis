package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.StudentExamHistoryRegisterDto;
import com.ccadmin.app.schooldata.model.dto.StudentExamHistoryRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.StudentExamHistoryEntity;
import com.ccadmin.app.schooldata.service.StudentExamHistoryService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/student-exam-history")
public class StudentExamHistoryController  {

    @Autowired
    private StudentExamHistoryService studentExamHistoryService;

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<StudentExamHistoryEntity> result = this.studentExamHistoryService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDetailById")
    public ResponseEntity<ResponseWsDto> findDetailById(@RequestParam int HistoryID) {
        try {
            return new ResponseEntity<>(this.studentExamHistoryService.findDetailById(HistoryID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody StudentExamHistoryRegisterDto registerDto) {
        try {
            StudentExamHistoryRegisterDto saved = this.studentExamHistoryService.save(registerDto);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody StudentExamHistoryRegisterMassiveDto registerMassive) {
        try {
            ResponseWsDto response = this.studentExamHistoryService.saveAll(registerMassive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam int HistoryID) {
        try {
            return new ResponseEntity<>(this.studentExamHistoryService.findDataForm(HistoryID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findByStudentID")
    public ResponseEntity<ResponseWsDto> findByStudentID(@RequestParam String StudentID) {
        try {
            return new ResponseEntity<>(this.studentExamHistoryService.findByStudentID(StudentID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}
