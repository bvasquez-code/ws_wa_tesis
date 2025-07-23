package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.ExerciseRegisterDto;
import com.ccadmin.app.schooldata.model.dto.ExerciseRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.ExerciseEntity;
import com.ccadmin.app.schooldata.model.entity.TopicEntity;
import com.ccadmin.app.schooldata.repository.ExerciseRepository;
import com.ccadmin.app.schooldata.repository.TopicRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ImageStorageService imageStorageService;

    private SearchTService<ExerciseEntity> searchTService;

    public ExerciseEntity findById(int ExerciseID) {
        Optional<ExerciseEntity> exercise = this.exerciseRepository.findById(ExerciseID);
        return exercise.orElse(null);
    }

    public ResponsePageSearchT<ExerciseEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.exerciseRepository);
        SearchDto search = new SearchDto(Query, Page);
        // Se asume un límite fijo de 10 registros por página
        return this.searchTService.findAll(search, 10);
    }

    @Transactional
    public ExerciseRegisterDto save(ExerciseRegisterDto exerciseRegister) throws IOException {
        String userCod = "admin";  // Reemplazar por el usuario de sesión actual
        if(exerciseRegister.base64Image!=null && !exerciseRegister.base64Image.isEmpty()){
            String ImagePath = this.imageStorageService.saveImage(exerciseRegister.base64Image);
            exerciseRegister.exercise.ImagePath = ImagePath;
        }
        if(exerciseRegister.exercise.ExerciseID==0){
            exerciseRegister.exercise.ExerciseCod = exerciseRepository.getExerciseCod();
        }
        exerciseRegister.exercise.session(userCod).validate();
        this.exerciseRepository.save(exerciseRegister.exercise);
        return exerciseRegister;
    }

    @Transactional
    public ResponseWsDto saveAll(ExerciseRegisterMassiveDto exerciseRegisterMassive) {
        ResponseWsDto rpt = new ResponseWsDto();
        ExerciseRegisterMassiveDto registerMassiveFail = new ExerciseRegisterMassiveDto();
        ExerciseRegisterMassiveDto registerMassiveOk = new ExerciseRegisterMassiveDto();

        String userCod = "admin";  // Reemplazar por el usuario de sesión actual

        for (var exerciseRegister : exerciseRegisterMassive.exerciseList) {
            try {
                exerciseRegister.exercise.session(userCod).validate();
                // Si el ExerciseID es distinto de 0 y ya existe, se considera duplicado
                if (exerciseRegister.exercise.ExerciseID != 0 && this.exerciseRepository.existsById(exerciseRegister.exercise.ExerciseID)) {
                    registerMassiveFail.exerciseList.add(exerciseRegister);
                } else {
                    registerMassiveOk.exerciseList.add(exerciseRegister);
                }
            } catch (Exception ex) {
                registerMassiveFail.exerciseList.add(exerciseRegister);
            }
        }

        List<ExerciseEntity> exerciseListOk = registerMassiveOk.exerciseList
                .stream()
                .map(dto -> dto.exercise)
                .collect(Collectors.toList());

        this.exerciseRepository.saveAll(exerciseListOk);
        rpt.AddResponseAdditional("registerMassiveOk", registerMassiveOk);
        rpt.AddResponseAdditional("registerMassiveFail", registerMassiveFail);
        return rpt;
    }

    public ResponseWsDto findDataForm(int ExerciseID) throws IOException {
        ResponseWsDto rpt = new ResponseWsDto();
        List<TopicEntity> topicList = this.topicRepository.findAll().stream().filter( e -> e.Status.equals("A") ).toList();
        rpt.AddResponseAdditional("topicList", topicList);

        List<String> courses = topicList.stream()
                .map(e->e.Course)
                .distinct()
                .toList();
        rpt.AddResponseAdditional("courses", courses);

        if (ExerciseID != 0) {
            ExerciseRegisterDto exerciseRegister = new ExerciseRegisterDto();
            exerciseRegister.exercise = this.findById(ExerciseID);
            if(exerciseRegister.exercise.ImagePath!=null && !exerciseRegister.exercise.ImagePath.isEmpty()){
                exerciseRegister.base64Image = this.imageStorageService.loadImageAsBase64(exerciseRegister.exercise.ImagePath);
            }
            rpt.AddResponseAdditional("exercise", exerciseRegister);

            TopicEntity currentTopics = topicList.stream().filter( e -> e.TopicID == exerciseRegister.exercise.TopicID ).findFirst().orElse(null);

            List<TopicEntity> filteredTopics = topicList.stream().filter( e -> e.Course.equals(currentTopics.Course)).toList();
            rpt.AddResponseAdditional("filteredTopics", filteredTopics);
        }else{
            List<TopicEntity> filteredTopics = topicList.stream().filter( e -> e.Course.equals(courses.get(0))).toList();
            rpt.AddResponseAdditional("filteredTopics", filteredTopics);
        }
        // Se pueden agregar otros catálogos o datos adicionales en el futuro
        return rpt;
    }
}