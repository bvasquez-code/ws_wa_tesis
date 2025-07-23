package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "data_topics")
public class TopicEntity extends AuditTableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int TopicID;
    public String TopicCod;
    public String Name;
    public String Course;

    public TopicEntity() {}

    /**
     * Valida que los campos obligatorios no sean nulos o vacíos.
     * Lanza EntityBuildException en caso de error.
     */
    public TopicEntity validate() {
        if (this.TopicCod == null || this.TopicCod.isEmpty()) {
            throw new EntityBuildException("TopicCod no puede ser nulo o vacío");
        }
        if (this.Name == null || this.Name.isEmpty()) {
            throw new EntityBuildException("Name no puede ser nulo o vacío");
        }
        if (this.Course == null || this.Course.isEmpty()) {
            throw new EntityBuildException("Course no puede ser nulo o vacío");
        }
        return this;
    }

    @Override
    public TopicEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "TopicEntity{" +
                "TopicID=" + TopicID +
                ", TopicCod='" + TopicCod + '\'' +
                ", Name='" + Name + '\'' +
                ", Course='" + Course + '\'' +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}