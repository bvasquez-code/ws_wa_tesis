package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.StudentTopicPerformanceRegisterDto;
import com.ccadmin.app.schooldata.model.entity.StudentTopicPerformanceEntity;
import com.ccadmin.app.schooldata.model.entity.id.StudentTopicPerformanceId;
import com.ccadmin.app.schooldata.repository.StudentTopicPerformanceRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import com.ccadmin.app.shared.service.SessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentTopicPerformanceService extends SessionService {

    @Autowired
    private StudentTopicPerformanceRepository studentTopicPerformanceRepository;

    private SearchTService<StudentTopicPerformanceEntity> searchTService;

    /**
     * Lista registros paginados basados en una cadena de búsqueda.
     */
    public ResponsePageSearchT<StudentTopicPerformanceEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.studentTopicPerformanceRepository);
        SearchDto search = new SearchDto(Query, Page,getUserCod(),"");
        // Se asume un límite fijo de 10 registros por página.
        return this.searchTService.findAllUserCod(search);
    }

    /**
     * Retorna el detalle de un registro basándose en StudentID y TopicID.
     */
    public ResponseWsDto findDetailById(String StudentID, int TopicID) {
        ResponseWsDto rpt = new ResponseWsDto();
        StudentTopicPerformanceId id = new StudentTopicPerformanceId(StudentID, TopicID);
        Optional<StudentTopicPerformanceEntity> optPerformance = this.studentTopicPerformanceRepository.findById(id);
        rpt.AddResponseAdditional("performance", optPerformance.orElse(null));
        return rpt;
    }

    /**
     * Guarda o actualiza un registro de desempeño.
     */
    @Transactional
    public StudentTopicPerformanceRegisterDto save(StudentTopicPerformanceRegisterDto registerDto) {
        String userCod = "admin"; // Reemplazar por el usuario de sesión actual
        registerDto.performance.session(userCod).validate();
        this.studentTopicPerformanceRepository.save(registerDto.performance);
        return registerDto;
    }

    /**
     * Elimina un registro de desempeño.
     */
    @Transactional
    public ResponseWsDto delete(StudentTopicPerformanceRegisterDto registerDto) {
        ResponseWsDto rpt = new ResponseWsDto();
        StudentTopicPerformanceId id = new StudentTopicPerformanceId(registerDto.performance.StudentID, registerDto.performance.TopicID);
        if(this.studentTopicPerformanceRepository.existsById(id)) {
            this.studentTopicPerformanceRepository.deleteById(id);
            rpt.AddResponseAdditional("message", "Registro eliminado exitosamente");
        } else {
            rpt.AddResponseAdditional("message", "Registro no encontrado");
        }
        return rpt;
    }
}