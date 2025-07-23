package com.ccadmin.app.schooldata.model.idto;

public interface IStudentExamAttemptInfoDto {

    String getCourse();
    Integer getTopicID();
    String getTopicName();
    Integer getNumExercisesAttempted();
    Integer getNumSolvedCorrectly();
}
