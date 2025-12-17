package com.ccadmin.app.schooldata.service;


import com.ccadmin.app.person.model.entity.PersonEntity;
import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.schooldata.model.dto.StudentExamHistoryStatusDto;
import com.ccadmin.app.schooldata.model.dto.StudentRegisterDto;
import com.ccadmin.app.schooldata.model.dto.StudentRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.dto.StudentRegisterWithUserDto;
import com.ccadmin.app.schooldata.model.entity.StudentEntity;
import com.ccadmin.app.schooldata.model.idto.IStudentExamPointsSummaryDto;
import com.ccadmin.app.schooldata.repository.StudentExamHistoryRepository;
import com.ccadmin.app.schooldata.repository.StudentRepository;
import com.ccadmin.app.security.model.dto.SessionStorageDto;
import com.ccadmin.app.security.model.entity.AppUserEntity;
import com.ccadmin.app.security.model.entity.UserProfileEntity;
import com.ccadmin.app.security.service.AppUserService;
import com.ccadmin.app.security.service.SecurityService;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import com.ccadmin.app.shared.service.SessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService extends SessionService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentExamHistoryRepository studentExamHistoryRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private SecurityService securityService;

    private SearchTService searchTService;

    public StudentEntity findById(String StudentID) {
        Optional<StudentEntity> student = this.studentRepository.findById(StudentID);
        return student.orElse(null);
    }

    public ResponsePageSearchT<StudentEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.studentRepository);
        SearchDto search = new SearchDto(Query, Page);
        // Se asume un límite fijo de 10 registros por página
        return this.searchTService.findAll(search, 10);
    }

    @Transactional
    public StudentRegisterDto save(StudentRegisterDto studentRegister) {
        // Se utiliza un usuario de sesión (aquí se usa "admin" de forma estática; reemplaza según convenga)

        if(studentRegister.student.StudentID.isEmpty()){
            studentRegister.student.StudentID = this.studentRepository.getStudentCodNew();
        }

        String userCod = getUserCod();
        studentRegister.student.session(userCod).validate();
        this.studentRepository.save(studentRegister.student);
        return studentRegister;
    }

    @Transactional
    public ResponseWsDto saveAll(StudentRegisterMassiveDto studentRegisterMassive) {
        ResponseWsDto rpt = new ResponseWsDto();
        StudentRegisterMassiveDto registerMassiveFail = new StudentRegisterMassiveDto();
        StudentRegisterMassiveDto registerMassiveOk = new StudentRegisterMassiveDto();

        String userCod = "admin"; // Reemplazar por el usuario de sesión

        for(var studentRegister : studentRegisterMassive.studentList) {
            try {
                studentRegister.student.session(userCod).validate();
                if(this.studentRepository.existsById(studentRegister.student.StudentID)) {
                    registerMassiveFail.studentList.add(studentRegister);
                } else {
                    registerMassiveOk.studentList.add(studentRegister);
                }
            } catch(Exception ex) {
                registerMassiveFail.studentList.add(studentRegister);
            }
        }

        List<StudentEntity> studentListOk = registerMassiveOk.studentList
                .stream()
                .map(dto -> dto.student)
                .collect(Collectors.toList());

        this.studentRepository.saveAll(studentListOk);
        rpt.AddResponseAdditional("registerMassiveOk", registerMassiveOk);
        rpt.AddResponseAdditional("registerMassiveFail", registerMassiveFail);
        return rpt;
    }

    public ResponseWsDto findDataForm(String StudentID) {
        ResponseWsDto rpt = new ResponseWsDto();

        if(StudentID != null && !StudentID.isEmpty()){
            StudentRegisterDto studentRegister = new StudentRegisterDto();
            studentRegister.student = this.findById(StudentID);
            rpt.AddResponseAdditional("student", studentRegister);
        }
        return rpt;
    }

    @Transactional
    public ResponseWsDto registerStudentWithUser(StudentRegisterWithUserDto dto) {
        ResponseWsDto rpt = new ResponseWsDto();
        String userCod = getUserCod(); // Reemplazar con el usuario de sesión actual

        // Se asume que dto.student.StudentID viene informado; de lo contrario se debe generar
        String studentId = dto.student.StudentID;
        if (studentId == null || studentId.isEmpty()) {
            throw new EntityBuildException("StudentID es obligatorio");
        }

        StudentEntity student = this.studentRepository.findById(dto.student.StudentID).get();

        // Crear registro en PERSON (person)
        PersonEntity person = new PersonEntity();
        person.PersonCod = studentId;
        person.PersonType = "01";
        person.DocumentType = "01";
        person.DocumentNum = dto.documentNum;
        person.Names = student.FirstName;
        person.LastNames = student.LastName;
        person.Phone = student.PhoneNumber;
        person.CellPhone = student.PhoneNumber;
        person.Email = student.Email;
        person.CreationUser = userCod;

        // Crear registro en USER_PROFILE (user_profile) con ProfileCod = "estudiante"
        UserProfileEntity userProfile = new UserProfileEntity();
        userProfile.UserCod = studentId;
        userProfile.ProfileCod = "student";
        userProfile.CreationUser = userCod;

        // Crear registro en APP_USER (app_user)
        AppUserEntity appUser = new AppUserEntity();
        appUser.UserCod = studentId;         // Igual al StudentID
        appUser.PersonCod = studentId;         // Referencia a la persona creada
        appUser.PasswordDecoded = dto.password;       // En un entorno real, hashear la contraseña
        appUser.Email = student.Email;
        appUser.CreationCode = "00000000";      // Valor por defecto o generado
        appUser.CreationUser = userCod;
        appUser.DateExpire = new Date();
        appUser.UserProfileList = List.of(userProfile);
        appUser.Person = person;

        appUserService.save(appUser);

        rpt.AddResponseAdditional("message", "Estudiante y usuario creados exitosamente");
        return rpt;
    }

    public StudentExamHistoryStatusDto getExamHistoryStatus(String studentId) {

        SessionStorageDto sessionStorage =  securityService.findUserSession();

        StudentExamHistoryStatusDto dto = new StudentExamHistoryStatusDto();
        dto.StudentID = studentId;

        boolean isMaster = this.securityService.userHasTheProfile(studentId,"maestro");

        int total = this.studentExamHistoryRepository.countByStudentId(studentId);
        dto.TotalAttempts = total;
        dto.HasHistory = total > 0;

        if (dto.HasHistory) {
            var last = this.studentExamHistoryRepository.findLastByStudentId(studentId);
            dto.LastHistoryID = last.HistoryID;
            dto.LastExamID = last.ExamID;

            int completed = this.studentExamHistoryRepository.countCompletedByStudentId(studentId);
            dto.CompletedAttempts = completed;
            dto.InProgressAttempts = total - completed;
            dto.IsMaster = isMaster;
        } else {
            dto.LastHistoryID = null;
            dto.LastExamID = null;
            dto.CompletedAttempts = 0;
            dto.InProgressAttempts = 0;
            dto.IsMaster = isMaster;
        }
        return dto;
    }

    public java.util.List<StudentEntity> findByCreationUser(String creationUser) {
        return this.studentRepository.findByCreationUser(creationUser);
    }

    public java.util.List<IStudentExamPointsSummaryDto> findExamPointsSummary(String studentId) {
        return this.studentRepository.findExamPointsSummary(studentId);
    }
}
