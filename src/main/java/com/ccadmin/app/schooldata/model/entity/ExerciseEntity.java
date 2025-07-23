package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "data_exercises")
public class ExerciseEntity extends AuditTableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ExerciseID;
    public String ExerciseCod;
    public int TopicID;
    public int Level;
    public String ImagePath;
    public String CorrectAnswer;

    public ExerciseEntity() {
    }

    /**
     * Valida los campos obligatorios de la entidad.
     * Lanza EntityBuildException si algún campo es nulo o inválido.
     */
    public ExerciseEntity validate() {
        if (this.ExerciseCod == null || this.ExerciseCod.isEmpty()) {
            throw new EntityBuildException("ExerciseCod no puede ser nulo o vacío");
        }
        if (this.TopicID <= 0) {
            throw new EntityBuildException("TopicID debe ser mayor a 0");
        }
        if (this.Level <= 0) {
            throw new EntityBuildException("Level debe ser mayor a 0");
        }
        if (this.ImagePath == null || this.ImagePath.isEmpty()) {
            throw new EntityBuildException("ImagePath no puede ser nulo o vacío");
        }
        if (this.CorrectAnswer == null || this.CorrectAnswer.isEmpty()) {
            throw new EntityBuildException("CorrectAnswer no puede ser nulo o vacío");
        }
        return this;
    }

    @Override
    public ExerciseEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "ExerciseEntity{" +
                "ExerciseID=" + ExerciseID +
                ", ExerciseCod='" + ExerciseCod + '\'' +
                ", TopicID=" + TopicID +
                ", Level=" + Level +
                ", ImagePath='" + ImagePath + '\'' +
                ", CorrectAnswer='" + CorrectAnswer + '\'' +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}