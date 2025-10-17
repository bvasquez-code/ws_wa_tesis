package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.TopicRegisterDto;
import com.ccadmin.app.schooldata.model.dto.TopicRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.TopicEntity;
import com.ccadmin.app.schooldata.repository.CourseRepository;
import com.ccadmin.app.schooldata.repository.TopicRepository;
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
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CourseRepository courseRepository;

    private SearchTService<TopicEntity> searchTService;

    public TopicEntity findById(int TopicID) {
        Optional<TopicEntity> topic = this.topicRepository.findById(TopicID);
        return topic.orElse(null);
    }

    public ResponsePageSearchT<TopicEntity> findAll(String Query, int Page) {
        this.searchTService = new SearchTService<>(this.topicRepository);
        SearchDto search = new SearchDto(Query, Page);
        // Se asume un límite fijo de 10 registros por página
        return this.searchTService.findAll(search, 10);
    }

    @Transactional
    public TopicRegisterDto save(TopicRegisterDto topicRegister) {
        String userCod = "admin"; // Reemplazar con el usuario de sesión actual
        topicRegister.topic.session(userCod).validate();
        this.topicRepository.save(topicRegister.topic);
        return topicRegister;
    }

    @Transactional
    public ResponseWsDto saveAll(TopicRegisterMassiveDto topicRegisterMassive) {
        ResponseWsDto rpt = new ResponseWsDto();
        TopicRegisterMassiveDto registerMassiveFail = new TopicRegisterMassiveDto();
        TopicRegisterMassiveDto registerMassiveOk = new TopicRegisterMassiveDto();

        String userCod = "admin"; // Reemplazar con el usuario de sesión actual

        for (var topicRegister : topicRegisterMassive.topicList) {
            try {
                topicRegister.topic.session(userCod).validate();
                if (this.topicRepository.existsById(topicRegister.topic.TopicID)) {
                    registerMassiveFail.topicList.add(topicRegister);
                } else {
                    registerMassiveOk.topicList.add(topicRegister);
                }
            } catch(Exception ex) {
                registerMassiveFail.topicList.add(topicRegister);
            }
        }

        List<TopicEntity> topicListOk = registerMassiveOk.topicList
                .stream()
                .map(dto -> dto.topic)
                .collect(Collectors.toList());

        this.topicRepository.saveAll(topicListOk);
        rpt.AddResponseAdditional("registerMassiveOk", registerMassiveOk);
        rpt.AddResponseAdditional("registerMassiveFail", registerMassiveFail);
        return rpt;
    }

    @Transactional
    public ResponseWsDto findDataForm(int TopicID) {
        ResponseWsDto rpt = new ResponseWsDto();
        if (TopicID != 0) {
            TopicRegisterDto topicRegister = new TopicRegisterDto();
            topicRegister.topic = this.findById(TopicID);
            rpt.AddResponseAdditional("topic", topicRegister);
            rpt.AddResponseAdditional("course",this.courseRepository.findAllActive());
        }
        return rpt;
    }
}