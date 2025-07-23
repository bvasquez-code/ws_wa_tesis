package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "data_exams")
public class ExamEntity extends AuditTableEntity implements Serializable {

    @Id
    public String ExamID;
    public String ExamName;
    public String Description;
    public String Subject;
    public int DurationMinutes;

    public ExamEntity() {
    }

    /**
     * Método para validar la información del examen.
     * Lanza ExamBuildException si algún campo obligatorio es nulo o inválido.
     */
    public ExamEntity validate() {
        if(this.ExamID == null || this.ExamID.isEmpty()){
            throw new EntityBuildException("ExamID no puede estar vacío");
        }
        if(this.ExamName == null || this.ExamName.isEmpty()){
            throw new EntityBuildException("ExamName no puede estar vacío");
        }
        if(this.Subject == null || this.Subject.isEmpty()){
            throw new EntityBuildException("Subject no puede estar vacío");
        }
        if(this.DurationMinutes <= 0){
            throw new EntityBuildException("DurationMinutes debe ser mayor a 0");
        }
        return this;
    }

    @Override
    public ExamEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "ExamEntity{" +
                "ExamID='" + ExamID + '\'' +
                ", ExamName='" + ExamName + '\'' +
                ", Description='" + Description + '\'' +
                ", Subject='" + Subject + '\'' +
                ", DurationMinutes=" + DurationMinutes +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}
