package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.CourseRegisterDto;
import com.ccadmin.app.schooldata.model.dto.CourseRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.CourseEntity;
import com.ccadmin.app.schooldata.service.CourseService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam String Course) {
        try {
            CourseEntity data = this.courseService.findById(Course);
            return new ResponseEntity<>(new ResponseWsDto(data), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<CourseEntity> result = this.courseService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam(required = false) String Course) {
        try {
            return new ResponseEntity<>(this.courseService.findDataForm(Course), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody CourseRegisterDto request) {
        try {
            CourseRegisterDto saved = this.courseService.save(request);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody CourseRegisterMassiveDto request) {
        try {
            var response = this.courseService.saveAll(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAllActive")
    public ResponseEntity<ResponseWsDto> findAllActive() {
        try {
            var list = this.courseService.findAllActive();
            return new ResponseEntity<>(new ResponseWsDto(list), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}
