package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.*;
import com.ccadmin.app.schooldata.model.entity.*;
import com.ccadmin.app.schooldata.model.idto.ICourseWeaknessDTO;
import com.ccadmin.app.schooldata.model.idto.IExamResultStatsDto;
import com.ccadmin.app.schooldata.model.idto.IStudentExamAttemptInfoDto;
import com.ccadmin.app.schooldata.repository.*;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamExerciseRepository examExerciseRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private StudentExamHistoryRepository studentExamHistoryRepository;

    @Autowired
    public StudentTopicPerformanceRepository studentTopicPerformanceRepository;
    @Autowired
    public TopicRepository topicRepository;
    @Autowired
    public ImageStorageService imageStorageService;

    private SearchTService<ExamEntity> searchTService;

    public ExamEntity findById(String ExamID) {
        Optional<ExamEntity> exam = this.examRepository.findById(ExamID);
        return exam.orElse(null);
    }

    public ResponsePageSearchT<ExamEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.examRepository);
        SearchDto search = new SearchDto(Query, Page);
        // Se asume un límite fijo de 10 registros por página
        return this.searchTService.findAll(search, 10);
    }

    @Transactional
    public ExamRegisterDto save(ExamRegisterDto examRegister) {
        // Se utiliza un usuario de sesión (se usa "admin" de forma estática; reemplazar según convenga)
        String userCod = "admin";
        examRegister.exam.session(userCod).validate();
        this.examRepository.save(examRegister.exam);
        return examRegister;
    }

    @Transactional
    public ResponseWsDto saveAll(ExamRegisterMassiveDto examRegisterMassive) {
        ResponseWsDto rpt = new ResponseWsDto();
        ExamRegisterMassiveDto registerMassiveFail = new ExamRegisterMassiveDto();
        ExamRegisterMassiveDto registerMassiveOk = new ExamRegisterMassiveDto();

        String userCod = "admin"; // Reemplazar por el usuario de sesión actual

        for(var examRegister : examRegisterMassive.examList) {
            try {
                examRegister.exam.session(userCod).validate();
                if(this.examRepository.existsById(examRegister.exam.ExamID)) {
                    registerMassiveFail.examList.add(examRegister);
                } else {
                    registerMassiveOk.examList.add(examRegister);
                }
            } catch(Exception ex) {
                registerMassiveFail.examList.add(examRegister);
            }
        }

        List<ExamEntity> examListOk = registerMassiveOk.examList
                .stream()
                .map(dto -> dto.exam)
                .collect(Collectors.toList());

        this.examRepository.saveAll(examListOk);
        rpt.AddResponseAdditional("registerMassiveOk", registerMassiveOk);
        rpt.AddResponseAdditional("registerMassiveFail", registerMassiveFail);
        return rpt;
    }

    public ResponseWsDto findDataForm(String ExamID,String StudentID,Integer HistoryID) {
        ResponseWsDto rpt = new ResponseWsDto();

        if(ExamID != null && !ExamID.isEmpty()){
            ExamRegisterDto examRegister = new ExamRegisterDto();
            examRegister.exam = this.findById(ExamID);
            examRegister.examExercises = this.examExerciseRepository.findByExamID(ExamID);
            rpt.AddResponseAdditional("exam", examRegister);

            List<ExerciseEntity> exercises = this.exerciseRepository.findAllById(
                examRegister.examExercises.stream()
                .map(e -> e.ExerciseID)
                .collect(Collectors.toList())
            );

            for(var exercise : exercises){
                if(exercise.ImagePath ==null || exercise.ImagePath.isEmpty()){
                    exercise.ImagePath = "9f144f73-72eb-4036-b882-dacb2f28c6c5.png";
                }
                try {
                    exercise.ImagePath = this.imageStorageService.loadImageAsBase64(exercise.ImagePath);
                }catch (Exception ex){
                    exercise.ImagePath = "";
                }
            }

            rpt.AddResponseAdditional("exercisesDataInfo", exercises);
        }

        if(HistoryID!=null && HistoryID!=0){
            StudentExamHistoryEntity studentExamHistory = this.studentExamHistoryRepository.findById(HistoryID).get();
            rpt.AddResponseAdditional("studentExamHistory", studentExamHistory);
        }else{
            if(StudentID!=null && !StudentID.isEmpty()){
                StudentExamHistoryEntity studentExamHistory = this.studentExamHistoryRepository.findLastExamHistoryByStudentIDExamID(ExamID,StudentID);
                rpt.AddResponseAdditional("studentExamHistory", studentExamHistory);
            }
        }
        // Se pueden agregar datos adicionales si fuese necesario, por ejemplo, listas de materias u otros catálogos.
        return rpt;
    }

    public StudentWeakTopicsResponseDTO getStudentWeakTopics(String studentId) {
        // Se obtienen los registros de student_topic_performance para el alumno.
        List<StudentTopicPerformanceEntity> performances = studentTopicPerformanceRepository.findByStudentID(studentId);
        List<WeakTopicDTO> topicsList = new ArrayList<>();

        for (StudentTopicPerformanceEntity perf : performances) {
            int topicId = perf.TopicID; // Atributo público en la entidad.
            BigDecimal avgPointsBD = perf.AveragePoints;
            double avgPoints = avgPointsBD.doubleValue();

            // Consultar en data_exam_results para obtener total de intentos y fallos para este tópico.
            IExamResultStatsDto stats = examRepository.getStatsByStudentAndTopic(studentId, topicId);
            if (stats == null || stats.getTotalAttempts() == null || stats.getTotalAttempts() == 0) {
                continue; // Si no hay intentos, se omite este tópico.
            }
            int totalAttempts = stats.getTotalAttempts();
            int failedAttempts = (stats.getFailedAttempts() != null) ? stats.getFailedAttempts() : 0;
            double failureRate = (double) failedAttempts / totalAttempts;
            double weight = Math.min(totalAttempts / 3.0, 1.0);
            double compositeScore = avgPoints * (1 - failureRate) * weight;

            // Obtener detalles del tópico desde data_topics.
            Optional<TopicEntity> topicOpt = topicRepository.findById(topicId);
            if (!topicOpt.isPresent()) continue;
            TopicEntity topic = topicOpt.get();

            WeakTopicDTO dto = new WeakTopicDTO();
            dto.TopicID = topicId;
            dto.TopicName = topic.Name;
            dto.Course = topic.Course;
            dto.AveragePoints = avgPoints;
            dto.TotalAttempts = totalAttempts;
            dto.FailureRate = failureRate;
            dto.CompositeScore = compositeScore;
            topicsList.add(dto);
        }

        // Ordenar la lista por compositeScore (los más débiles tendrán menor score).
        topicsList.sort(Comparator.comparingDouble(t -> t.CompositeScore));
        List<WeakTopicDTO> weakestTopics = topicsList.stream().limit(100).collect(Collectors.toList());

        // Agrupar por curso y calcular el promedio de compositeScore para cada curso.
        Map<String, List<WeakTopicDTO>> topicsByCourse = topicsList.stream()
                .collect(Collectors.groupingBy(t -> t.Course));
        List<CourseWeaknessDTO> courseRanking = new ArrayList<>();
        for (Map.Entry<String, List<WeakTopicDTO>> entry : topicsByCourse.entrySet()) {
            String course = entry.getKey();
            double avgComposite = entry.getValue().stream().mapToDouble(t -> t.CompositeScore).average().orElse(0);
            CourseWeaknessDTO cw = new CourseWeaknessDTO();
            cw.Course = course;
            cw.AveragePerformance = avgComposite;
            courseRanking.add(cw);
        }
        courseRanking.sort(Comparator.comparingDouble(c -> c.AveragePerformance));

        List<IStudentExamAttemptInfoDto> attemptInfo = topicRepository.findStudentExamAttemptInfo(studentId);

        List<ICourseWeaknessDTO> ICourseRanking = topicRepository.findCourseWeakness(studentId);

        StudentWeakTopicsResponseDTO response = new StudentWeakTopicsResponseDTO();
        response.WeakestTopics = weakestTopics;
        response.CourseWeaknessRanking = ICourseRanking;
        response.ExamAttemptInfo = attemptInfo;
        return response;
    }
}