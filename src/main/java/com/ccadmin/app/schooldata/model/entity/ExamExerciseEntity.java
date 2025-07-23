package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.schooldata.model.entity.id.ExamExerciseID;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "data_exam_exercises")
@IdClass(ExamExerciseID.class)
public class ExamExerciseEntity extends AuditTableEntity implements Serializable {

    @Id
    public String ExamID;      // Identificador para el examen
    @Id
    public int ExerciseID;     // Identificador para el ejercicio

    public int TopicID;
    public String DifficultyLevel;
    public int Points;

    public ExamExerciseEntity() {}

    /**
     * Valida que los campos obligatorios no sean nulos o inválidos.
     * Lanza EntityBuildException en caso de error.
     */
    public ExamExerciseEntity validate() {
        if (ExamID == null || ExamID.isEmpty()) {
            throw new EntityBuildException("ExamID no puede ser nulo o vacío");
        }
        if (ExerciseID <= 0) {
            throw new EntityBuildException("ExerciseID debe ser mayor a 0");
        }
        if (TopicID <= 0) {
            throw new EntityBuildException("TopicID debe ser mayor a 0");
        }
        if (DifficultyLevel == null || DifficultyLevel.isEmpty()) {
            throw new EntityBuildException("DifficultyLevel no puede ser nulo o vacío");
        }
        if (Points < 0) {
            throw new EntityBuildException("Points no puede ser negativo");
        }
        return this;
    }

    @Override
    public ExamExerciseEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "ExamExerciseEntity{" +
                "ExamID='" + ExamID + '\'' +
                ", ExerciseID=" + ExerciseID +
                ", TopicID=" + TopicID +
                ", DifficultyLevel='" + DifficultyLevel + '\'' +
                ", Points=" + Points +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}
