package com.ccadmin.app.schooldata.model.entity;

import com.ccadmin.app.schooldata.exception.EntityBuildException;
import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.io.Serializable;

@Entity
@Table(name = "data_exercise_images")
public class ExerciseImageEntity extends AuditTableEntity implements Serializable {

    @Id
    @Column(name = "ImagePath", length = 512, nullable = false)
    public String ImagePath;

    @Lob
    @Column(name = "ImageBase64", nullable = false, columnDefinition = "LONGTEXT")
    public String ImageBase64;

    public ExerciseImageEntity() {
    }

    public ExerciseImageEntity(String imagePath, String imageBase64) {
        ImagePath = imagePath;
        ImageBase64 = imageBase64;
    }

    /**
     * Valida los campos obligatorios de la entidad.
     * Lanza EntityBuildException si algún campo es nulo o inválido.
     */
    public ExerciseImageEntity validate() {
        if (this.ImagePath == null || this.ImagePath.isEmpty()) {
            throw new EntityBuildException("ImagePath no puede ser nulo o vacío");
        }
        if (this.ImageBase64 == null || this.ImageBase64.isEmpty()) {
            throw new EntityBuildException("ImageBase64 no puede ser nulo o vacío");
        }
        return this;
    }

    @Override
    public ExerciseImageEntity session(String userCod) {
        this.addSession(userCod);
        return this;
    }

    @Override
    public String toString() {
        return "ExerciseImageEntity{" +
                "ImagePath='" + ImagePath + '\'' +
                ", ImageBase64(length)=" + (ImageBase64 != null ? ImageBase64.length() : 0) +
                ", CreationUser='" + CreationUser + '\'' +
                ", CreationDate=" + CreationDate +
                ", ModifyUser='" + ModifyUser + '\'' +
                ", ModifyDate=" + ModifyDate +
                ", Status='" + Status + '\'' +
                '}';
    }
}
