package com.ccadmin.app.schooldata.model.dto;

import com.ccadmin.app.schooldata.model.entity.StudentEntity;

public class StudentRegisterWithUserDto {

    public StudentEntity student;
    // Datos para crear el usuario y persona:
    public String password;
    public String documentType;
    public String documentNum;
    public String ubigeoCod;
    public String cellPhone;
}
