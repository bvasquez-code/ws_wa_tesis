package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.DataAutoProcessEntity;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataAutoProcessRepository
        extends JpaRepository<DataAutoProcessEntity, String>, CcAdminRepository<DataAutoProcessEntity, String> {

    @Query(value = """
            select * from data_auto_process p
            where p.Status = 'A'
            order by p.ProcessCode
            """, nativeQuery = true)
    List<DataAutoProcessEntity> findAllActive();

    @Override
    @Query(value = """
            select count(1)
            from data_auto_process p
            where (p.ProcessCode = :id or p.ProcessName like %:query%)
            """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
            select *
            from data_auto_process p
            where (p.ProcessCode = :id or p.ProcessName like %:query%)
            order by p.CreationDate desc
            limit :init, :limit
            """, nativeQuery = true)
    List<DataAutoProcessEntity> findByQueryText(@Param("id") String id, @Param("query") String query,
                                                @Param("init") int init, @Param("limit") int limit);
}
