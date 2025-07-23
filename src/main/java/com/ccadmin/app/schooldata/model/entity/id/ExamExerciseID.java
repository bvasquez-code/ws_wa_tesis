package com.ccadmin.app.schooldata.model.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class ExamExerciseID implements Serializable {

    public String ExamID;
    public int ExerciseID;

    public ExamExerciseID() {}

    public ExamExerciseID(String examID, int exerciseID) {
        this.ExamID = examID;
        this.ExerciseID = exerciseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamExerciseID)) return false;
        ExamExerciseID that = (ExamExerciseID) o;
        return ExerciseID == that.ExerciseID &&
                Objects.equals(ExamID, that.ExamID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ExamID, ExerciseID);
    }

    @Override
    public String toString() {
        return "ExamExerciseId{" +
                "ExamID='" + ExamID + '\'' +
                ", ExerciseID=" + ExerciseID +
                '}';
    }
}