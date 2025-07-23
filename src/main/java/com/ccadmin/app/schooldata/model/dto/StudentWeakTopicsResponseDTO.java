package com.ccadmin.app.schooldata.model.dto;

import com.ccadmin.app.schooldata.model.idto.ICourseWeaknessDTO;
import com.ccadmin.app.schooldata.model.idto.IStudentExamAttemptInfoDto;

import java.util.List;
public class StudentWeakTopicsResponseDTO {
    public List<WeakTopicDTO> WeakestTopics;
    public List<ICourseWeaknessDTO> CourseWeaknessRanking;

    public List<IStudentExamAttemptInfoDto> ExamAttemptInfo;
}
