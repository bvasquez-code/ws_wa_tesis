package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.CourseRegisterDto;
import com.ccadmin.app.schooldata.model.dto.CourseRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.CourseEntity;
import com.ccadmin.app.schooldata.repository.CourseRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchTService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    private SearchTService<CourseEntity> searchTService;

    public CourseEntity findById(String Course) {
        return this.courseRepository.findById(Course).orElse(null);
    }

    public java.util.List<CourseEntity> findAllActive() {
        return this.courseRepository.findAllActive();
    }

    public ResponsePageSearchT<CourseEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.courseRepository);
        SearchDto search = new SearchDto(Query, Page);
        return this.searchTService.findAll(search, 10); // límite fijo de 10 por página
    }

    @Transactional
    public CourseRegisterDto save(CourseRegisterDto request) {
        String userCod = "admin"; // reemplazar por usuario de sesión
        request.course.session(userCod).validate();
        this.courseRepository.save(request.course);
        return request;
    }

    @Transactional
    public ResponseWsDto saveAll(CourseRegisterMassiveDto massive) {
        ResponseWsDto rpt = new ResponseWsDto();
        CourseRegisterMassiveDto ok = new CourseRegisterMassiveDto();
        CourseRegisterMassiveDto fail = new CourseRegisterMassiveDto();

        String userCod = "admin";

        for (var item : massive.list) {
            try {
                item.course.session(userCod).validate();
                if (this.courseRepository.existsById(item.course.Course)) {
                    fail.list.add(item);
                } else {
                    ok.list.add(item);
                }
            } catch (Exception ex) {
                fail.list.add(item);
            }
        }

        var toSave = ok.list.stream().map(d -> d.course).collect(Collectors.toList());
        this.courseRepository.saveAll(toSave);

        rpt.AddResponseAdditional("registerMassiveOk", ok);
        rpt.AddResponseAdditional("registerMassiveFail", fail);
        return rpt;
    }

    public ResponseWsDto findDataForm(String Course) {
        ResponseWsDto rpt = new ResponseWsDto();
        // catálogo base: lista de cursos activos
        var courseActiveList = this.courseRepository.findAllActive();
        rpt.AddResponseAdditional("courseList", courseActiveList);

        if (Course != null && !Course.isEmpty()) {
            CourseRegisterDto reg = new CourseRegisterDto();
            reg.course = this.findById(Course);
            rpt.AddResponseAdditional("course", reg);
        }
        return rpt;
    }
}
