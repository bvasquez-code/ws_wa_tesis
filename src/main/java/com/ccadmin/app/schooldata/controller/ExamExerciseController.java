package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.ExamExerciseRegisterDto;
import com.ccadmin.app.schooldata.model.dto.ExamExerciseRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.ExamExerciseEntity;
import com.ccadmin.app.schooldata.service.ExamExerciseService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/examExercise")
public class ExamExerciseController {

    @Autowired
    private ExamExerciseService examExerciseService;

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam String examID, @RequestParam int exerciseID) {
        try {
            ExamExerciseEntity examExercise = this.examExerciseService.findById(examID, exerciseID);
            return new ResponseEntity<>(new ResponseWsDto(examExercise), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<ExamExerciseEntity> result = this.examExerciseService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam String examID, @RequestParam int exerciseID) {
        try {
            return new ResponseEntity<>(this.examExerciseService.findDataForm(examID, exerciseID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody ExamExerciseRegisterDto examExerciseRegister) {
        try {
            ExamExerciseRegisterDto saved = this.examExerciseService.save(examExerciseRegister);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody ExamExerciseRegisterMassiveDto examExerciseRegisterMassive) {
        try {
            ResponseWsDto response = this.examExerciseService.saveAll(examExerciseRegisterMassive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}
