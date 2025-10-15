package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.model.dto.StudentRegisterDto;
import com.ccadmin.app.schooldata.model.dto.StudentRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.dto.StudentRegisterWithUserDto;
import com.ccadmin.app.schooldata.model.entity.StudentEntity;
import com.ccadmin.app.schooldata.service.StudentService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("findById")
    public ResponseEntity<ResponseWsDto> findById(@RequestParam String StudentID) {
        try {
            StudentEntity student = this.studentService.findById(StudentID);
            return new ResponseEntity<>(new ResponseWsDto(student), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<ResponseWsDto> findAll(@RequestParam String Query, @RequestParam int Page) {
        try {
            ResponsePageSearchT<StudentEntity> result = this.studentService.findAll(Query, Page);
            return new ResponseEntity<>(new ResponseWsDto(result), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<ResponseWsDto> save(@RequestBody StudentRegisterDto studentRegister) {
        try {
            StudentRegisterDto saved = this.studentService.save(studentRegister);
            return new ResponseEntity<>(new ResponseWsDto(saved), HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("saveAll")
    public ResponseEntity<ResponseWsDto> saveAll(@RequestBody StudentRegisterMassiveDto studentRegisterMassive) {
        try {
            ResponseWsDto response = this.studentService.saveAll(studentRegisterMassive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findDataForm")
    public ResponseEntity<ResponseWsDto> findDataForm(@RequestParam String StudentID) {
        try {
            return new ResponseEntity<ResponseWsDto>(
                    this.studentService.findDataForm(StudentID),
                    HttpStatus.OK
            );
        } catch (Exception ex) {
            return new ResponseEntity<ResponseWsDto>(
                    new ResponseWsDto(ex),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("registerStudent")
    public ResponseEntity<ResponseWsDto> registerStudent(@RequestBody StudentRegisterWithUserDto dto) {
        try {
            ResponseWsDto response = studentService.registerStudentWithUser(dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("checkHistory")
    public ResponseEntity<ResponseWsDto> checkHistory(@RequestParam String StudentID) {
        try {
            var status = this.studentService.getExamHistoryStatus(StudentID);
            return new ResponseEntity<>(new ResponseWsDto(status), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseWsDto(ex), HttpStatus.BAD_REQUEST);
        }
    }
}
