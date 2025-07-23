package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "data_students")
public class StudentEntity extends AuditTableEntity implements Serializable {

    @Id
    public String StudentID;
    public String FirstName;
    public String LastName;
    public Date BirthDate;
    public String Email;
    public String PhoneNumber;
    public String Address;
    public Date EnrollmentDate;
    public String GradeLevel;
    public String RegistrationUrl;

    public StudentEntity() {}

    public StudentEntity validate() {
        if(this.FirstName == null || this.FirstName.isEmpty()){
            throw new EntityBuildException("FirstName cannot be empty");
        }
        if(this.LastName == null || this.LastName.isEmpty()){
            throw new EntityBuildException("LastName cannot be empty");
        }
        if(this.EnrollmentDate == null){
            throw new EntityBuildException("EnrollmentDate cannot be null");
        }
        if(this.GradeLevel == null || this.GradeLevel.isEmpty()){
            throw new EntityBuildException("GradeLevel cannot be empty");
        }
        // Se pueden agregar más validaciones según se requiera
        return this;
    }

    @Override
    public StudentEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "StudentID='" + StudentID + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", BirthDate=" + BirthDate +
                ", Email='" + Email + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Address='" + Address + '\'' +
                ", EnrollmentDate=" + EnrollmentDate +
                ", GradeLevel='" + GradeLevel + '\'' +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}
