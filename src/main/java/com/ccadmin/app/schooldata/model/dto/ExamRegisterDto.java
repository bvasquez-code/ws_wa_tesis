package com.ccadmin.app.schooldata.model.dto;

import java.util.List;

import com.ccadmin.app.schooldata.model.entity.ExamEntity;
import com.ccadmin.app.schooldata.model.entity.ExamExerciseEntity;

public class ExamRegisterDto {
    public ExamEntity exam;
    public List<ExamExerciseEntity> examExercises;

    public ExamRegisterDto() {
    }

    public ExamRegisterDto(ExamEntity exam, List<ExamExerciseEntity> examExercises) {
        this.exam = exam;
        this.examExercises = examExercises;
    }
}
