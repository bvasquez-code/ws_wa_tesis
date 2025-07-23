package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.StudentExamHistoryRegisterDto;
import com.ccadmin.app.schooldata.model.dto.StudentExamHistoryRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.StudentExamHistoryEntity;
import com.ccadmin.app.schooldata.repository.StudentExamHistoryRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import com.ccadmin.app.shared.service.SessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentExamHistoryService  extends SessionService {
    @Autowired
    private StudentExamHistoryRepository studentExamHistoryRepository;

    private SearchTService<StudentExamHistoryEntity> searchTService;

    public StudentExamHistoryEntity findById(int HistoryID) {
        Optional<StudentExamHistoryEntity> history = this.studentExamHistoryRepository.findById(HistoryID);
        return history.orElse(null);
    }

    public ResponsePageSearchT<StudentExamHistoryEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.studentExamHistoryRepository);
        SearchDto search = new SearchDto(Query, Page,getUserCod(),"");
        // Se asume un límite fijo de 10 registros por página
        return this.searchTService.findAllUserCod(search);
    }

    public ResponseWsDto findDetailById(int HistoryID) {
        ResponseWsDto rpt = new ResponseWsDto();
        StudentExamHistoryEntity history = this.findById(HistoryID);
        rpt.AddResponseAdditional("history", history);
        return rpt;
    }

    @Transactional
    public StudentExamHistoryRegisterDto save(StudentExamHistoryRegisterDto registerDto) {
        String userCod = getUserCod(); // Reemplazar por el usuario de sesión actual
        registerDto.history.session(userCod).validate();
        this.studentExamHistoryRepository.save(registerDto.history);
        return registerDto;
    }

    @Transactional
    public ResponseWsDto saveAll(StudentExamHistoryRegisterMassiveDto registerMassive) {
        ResponseWsDto rpt = new ResponseWsDto();
        StudentExamHistoryRegisterMassiveDto registerMassiveFail = new StudentExamHistoryRegisterMassiveDto();
        StudentExamHistoryRegisterMassiveDto registerMassiveOk = new StudentExamHistoryRegisterMassiveDto();

        String userCod = "admin";

        for(var registerDto : registerMassive.historyList) {
            try {
                registerDto.history.session(userCod).validate();
                if(registerDto.history.HistoryID != 0 && this.studentExamHistoryRepository.existsById(registerDto.history.HistoryID)) {
                    registerMassiveFail.historyList.add(registerDto);
                } else {
                    registerMassiveOk.historyList.add(registerDto);
                }
            } catch(Exception ex) {
                registerMassiveFail.historyList.add(registerDto);
            }
        }

        List<StudentExamHistoryEntity> okList = registerMassiveOk.historyList
                .stream()
                .map(dto -> dto.history)
                .collect(Collectors.toList());

        this.studentExamHistoryRepository.saveAll(okList);
        rpt.AddResponseAdditional("registerMassiveOk", registerMassiveOk);
        rpt.AddResponseAdditional("registerMassiveFail", registerMassiveFail);
        return rpt;
    }

    @Transactional
    public ResponseWsDto findDataForm(int HistoryID) {
        ResponseWsDto rpt = new ResponseWsDto();
        if (HistoryID != 0) {
            StudentExamHistoryRegisterDto registerDto = new StudentExamHistoryRegisterDto();
            registerDto.history = this.findById(HistoryID);
            rpt.AddResponseAdditional("history", registerDto);
        }
        // Se pueden agregar otros catálogos o datos adicionales para el formulario
        return rpt;
    }

    public ResponseWsDto findByStudentID(String StudentID) {
        ResponseWsDto rpt = new ResponseWsDto();
        List<StudentExamHistoryEntity> list = this.studentExamHistoryRepository.findByStudentID(StudentID);
        rpt.AddResponseAdditional("historyList", list);
        return rpt;
    }
}
