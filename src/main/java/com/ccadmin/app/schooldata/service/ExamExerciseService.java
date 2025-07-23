package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.ExamExerciseRegisterDto;
import com.ccadmin.app.schooldata.model.dto.ExamExerciseRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.ExamExerciseEntity;
import com.ccadmin.app.schooldata.model.entity.id.ExamExerciseID;
import com.ccadmin.app.schooldata.repository.ExamExerciseRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamExerciseService {

    @Autowired
    private ExamExerciseRepository examExerciseRepository;

    private SearchTService<ExamExerciseEntity> searchTService;

    /**
     * Retorna la entidad ExamExercise según el ExamID y ExerciseID.
     */
    public ExamExerciseEntity findById(String examID, int exerciseID) {
        ExamExerciseID id = new ExamExerciseID(examID, exerciseID);
        Optional<ExamExerciseEntity> examExercise = this.examExerciseRepository.findById(id);
        return examExercise.orElse(null);
    }

    /**
     * Realiza una búsqueda paginada utilizando un query y retorna un ResponsePageSearchT.
     */
    public ResponsePageSearchT<ExamExerciseEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.examExerciseRepository);
        SearchDto search = new SearchDto(Query, Page);
        return this.searchTService.findAll(search, 10);
    }

    /**
     * Guarda (o actualiza) un registro individual de ExamExercise.
     */
    @Transactional
    public ExamExerciseRegisterDto save(ExamExerciseRegisterDto examExerciseRegister) {
        String userCod = "admin"; // Reemplazar por el usuario de sesión actual
        examExerciseRegister.examExercise.session(userCod).validate();
        this.examExerciseRepository.save(examExerciseRegister.examExercise);
        return examExerciseRegister;
    }

    /**
     * Guarda registros masivos y clasifica los que fallaron y los que se guardaron correctamente.
     */
    @Transactional
    public ResponseWsDto saveAll(ExamExerciseRegisterMassiveDto examExerciseRegisterMassive) {
        ResponseWsDto rpt = new ResponseWsDto();
        ExamExerciseRegisterMassiveDto registerMassiveFail = new ExamExerciseRegisterMassiveDto();
        ExamExerciseRegisterMassiveDto registerMassiveOk = new ExamExerciseRegisterMassiveDto();

        String userCod = "admin"; // Reemplazar por el usuario de sesión actual

        for (var examExerciseRegister : examExerciseRegisterMassive.examExerciseList) {
            try {
                examExerciseRegister.examExercise.session(userCod).validate();
                ExamExerciseID id = new ExamExerciseID(examExerciseRegister.examExercise.ExamID,examExerciseRegister.examExercise.ExerciseID);
                if (id != null && this.examExerciseRepository.existsById(id)) {
                    registerMassiveFail.examExerciseList.add(examExerciseRegister);
                } else {
                    registerMassiveOk.examExerciseList.add(examExerciseRegister);
                }
            } catch(Exception ex) {
                registerMassiveFail.examExerciseList.add(examExerciseRegister);
            }
        }

        List<ExamExerciseEntity> examExerciseListOk = registerMassiveOk.examExerciseList
                .stream()
                .map(dto -> dto.examExercise)
                .collect(Collectors.toList());

        this.examExerciseRepository.saveAll(examExerciseListOk);
        rpt.AddResponseAdditional("registerMassiveOk", registerMassiveOk);
        rpt.AddResponseAdditional("registerMassiveFail", registerMassiveFail);
        return rpt;
    }

    /**
     * Retorna los datos para el formulario (por ejemplo, la entidad registrada)
     * a partir del ExamID y ExerciseID.
     */
    public ResponseWsDto findDataForm(String examID, int exerciseID) {
        ResponseWsDto rpt = new ResponseWsDto();
        if (examID != null && !examID.isEmpty() && exerciseID > 0) {
            ExamExerciseRegisterDto examExerciseRegister = new ExamExerciseRegisterDto();
            examExerciseRegister.examExercise = this.findById(examID, exerciseID);
            rpt.AddResponseAdditional("examExercise", examExerciseRegister);
        }
        // Se pueden agregar otros datos adicionales en el futuro.
        return rpt;
    }
}
