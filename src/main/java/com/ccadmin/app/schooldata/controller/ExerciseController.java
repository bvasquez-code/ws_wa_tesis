package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.ExerciseRegisterDto;
import com.ccadmin.app.schooldata.model.dto.ExerciseRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.ExerciseEntity;
import com.ccadmin.app.schooldata.service.ExerciseService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam int ExerciseID) {
        try {
            ExerciseEntity exercise = this.exerciseService.findById(ExerciseID);
            return new ResponseEntity<>(new ResponseWsDto(exercise), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<ExerciseEntity> result = this.exerciseService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam int ExerciseID) {
        try {
            return new ResponseEntity<>(this.exerciseService.findDataForm(ExerciseID), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody ExerciseRegisterDto exerciseRegister) {
        try {
            ExerciseRegisterDto saved = this.exerciseService.save(exerciseRegister);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody ExerciseRegisterMassiveDto exerciseRegisterMassive) {
        try {
            ResponseWsDto response = this.exerciseService.saveAll(exerciseRegisterMassive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}