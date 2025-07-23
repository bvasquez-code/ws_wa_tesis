package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.schooldata.model.entity.id.StudentTopicPerformanceId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "student_topic_performance")
@IdClass(StudentTopicPerformanceId.class)
public class StudentTopicPerformanceEntity implements Serializable {

    @Id
    public String StudentID;

    @Id
    public int TopicID;

    // Promedio de puntos obtenidos (decimal(5,2))
    public BigDecimal AveragePoints;

    // Clasificación del desempeño (Alto, Medio, Bajo)
    public String PerformanceClassification;

    public StudentTopicPerformanceEntity() {}

    /**
     * Valida que los campos obligatorios no sean nulos o vacíos.
     */
    public StudentTopicPerformanceEntity validate() {
        if (StudentID == null || StudentID.isEmpty()) {
            throw new EntityBuildException("StudentID no puede ser nulo o vacío");
        }
        if (TopicID <= 0) {
            throw new EntityBuildException("TopicID debe ser mayor a 0");
        }
        if (AveragePoints == null) {
            throw new EntityBuildException("AveragePoints es obligatorio");
        }
        if (PerformanceClassification == null || PerformanceClassification.isEmpty()) {
            throw new EntityBuildException("PerformanceClassification no puede ser nulo o vacío");
        }
        return this;
    }

    /**
     * Método de sesión (puedes ampliar con lógica de auditoría si fuera necesario).
     */
    public StudentTopicPerformanceEntity session(String userCod) {
        // En este ejemplo no se manejan campos de auditoría, pero se deja este método por consistencia.
        return this;
    }

    @Override
    public String toString() {
        return "StudentTopicPerformanceEntity{" +
                "StudentID='" + StudentID + '\'' +
                ", TopicID=" + TopicID +
                ", AveragePoints=" + AveragePoints +
                ", PerformanceClassification='" + PerformanceClassification + '\'' +
                '}';
    }
}