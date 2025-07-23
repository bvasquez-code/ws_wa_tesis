package com.ccadmin.app.shared.repository;

import com.ccadmin.app.shared.model.entity.BusinessConfigEntity;
import com.ccadmin.app.shared.model.entity.id.BusinessConfigEntityID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessConfigRepository extends JpaRepository<BusinessConfigEntity, BusinessConfigEntityID> {


    @Query( value = """
            select bc.* from business_config bc
            where bc.GroupCod = :GroupCod
            and bc.ConfigCod = :ConfigCod      
            """, nativeQuery = true)
    public BusinessConfigEntity findByConfigCod(
             @Param("GroupCod") String GroupCod
            ,@Param("ConfigCod") String ConfigCod
    );

}
