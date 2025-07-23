package com.ccadmin.app.system.model.entity;

import com.ccadmin.app.shared.model.entity.AuditTableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table( name = "counterfoil" )
public class CounterfoilEntity extends AuditTableEntity implements Serializable {

    @Id
    public String CounterfoilCod;
    public String DocumentType;
    public String Series;
    public int Correlative;
    public String IsAutomatic;
    public String GroupDocument;

}
