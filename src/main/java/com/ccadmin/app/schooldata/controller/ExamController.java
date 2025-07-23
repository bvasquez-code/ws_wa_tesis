package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.ExamRegisterDto;
import com.ccadmin.app.schooldata.model.dto.ExamRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.dto.StudentWeakTopicsResponseDTO;
import com.ccadmin.app.schooldata.model.entity.ExamEntity;
import com.ccadmin.app.schooldata.service.ExamService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam String ExamID) {
        try {
            ExamEntity exam = this.examService.findById(ExamID);
            return new ResponseEntity<>(new ResponseWsDto(exam), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<ExamEntity> result = this.examService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam String ExamID,@RequestParam String StudentID,@RequestParam Integer HistoryID) {
        try {
            return new ResponseEntity<>(this.examService.findDataForm(ExamID,StudentID,HistoryID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody ExamRegisterDto examRegister) {
        try {
            ExamRegisterDto saved = this.examService.save(examRegister);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody ExamRegisterMassiveDto examRegisterMassive) {
        try {
            ResponseWsDto response = this.examService.saveAll(examRegisterMassive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getStudentWeakTopics")
    public ResponseEntity<ResponseWsDto> getStudentWeakTopics(@RequestParam String studentId) {
        try {
            ResponseWsDto response = new ResponseWsDto(examService.getStudentWeakTopics(studentId));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}