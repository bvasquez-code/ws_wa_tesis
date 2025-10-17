package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "data_courses")
public class CourseEntity extends AuditTableEntity implements Serializable {

    @Id
    @Column(name = "Course")
    public String Course; // PK (varchar 50)

    public CourseEntity() {}

    public CourseEntity validate() {
        if (this.Course == null || this.Course.trim().isEmpty()) {
            throw new EntityBuildException("Course no puede ser nulo o vacío");
        }
        if (this.Course.length() > 50) {
            throw new EntityBuildException("Course no debe exceder 50 caracteres");
        }
        return this;
    }

    @Override
    public CourseEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "Course='" + Course + '\'' +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}
