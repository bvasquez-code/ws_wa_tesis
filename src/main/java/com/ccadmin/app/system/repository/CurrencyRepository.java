package com.ccadmin.app.system.repository;

import com.ccadmin.app.system.model.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity,String> {

    @Query(value = """
            select * from currency c where c.IsCurrencySystem = 'S' order by c.CreationDate desc  limit 1
            """,nativeQuery = true)
    public CurrencyEntity findCurrencySystem();
    @Query(value = """
            select c.* from currency c where c.Status = 'A'
            """,nativeQuery = true)
    public List<CurrencyEntity> findAllActive();
}
