package com.ccadmin.app.system.repository;

import com.ccadmin.app.system.model.entity.CounterfoilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface CounterfoilRepository extends JpaRepository<CounterfoilEntity,String> {

    @Query( value = """
            select
            	c.*
            from counterfoil c
                inner join counterfoil_store cs on c.CounterfoilCod = cs.CounterfoilCod
            where
                c.DocumentType = :DocumentType
                and c.IsAutomatic = 'S'
                and cs.StoreCod = :StoreCod
            """, nativeQuery = true)
    public CounterfoilEntity findByStoreDefault(
             @Param("DocumentType") String DocumentType
            ,@Param("StoreCod") String StoreCod
    );

    @Query( value = """
            select
            	c.*
            from counterfoil c
                inner join counterfoil_store cs on c.CounterfoilCod = cs.CounterfoilCod
            where
                c.DocumentType = :DocumentType
                and c.IsAutomatic = 'S'
                and cs.StoreCod = :StoreCod
                and c.GroupDocument = :GroupDocument
            """, nativeQuery = true)
    public CounterfoilEntity findByStoreDefault(
             @Param("DocumentType") String DocumentType
            ,@Param("StoreCod") String StoreCod
            ,@Param("GroupDocument") String GroupDocument
    );
}
