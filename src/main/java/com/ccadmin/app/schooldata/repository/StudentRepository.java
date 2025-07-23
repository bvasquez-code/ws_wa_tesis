package com.ccadmin.app.schooldata.repository;


import com.ccadmin.app.schooldata.model.entity.StudentEntity;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface StudentRepository extends JpaRepository<StudentEntity, String>, CcAdminRepository<StudentEntity, String> {

    @Override
    @Query(value = """
            select count(1) from data_students s
            where s.StudentID = :id
            or s.FirstName like %:query%
            or s.LastName like %:query%
            """, nativeQuery = true)
    int countByQueryText(String id, String query);

    @Override
    @Query(value = """
            select * from data_students s
            where s.StudentID = :id
            or s.FirstName like %:query%
            or s.LastName like %:query%
            order by s.CreationDate desc
            limit :init, :limit
            """, nativeQuery = true)
    List<StudentEntity> findByQueryText(@Param("id") String id, @Param("query") String query, @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
            CALL db_sys_expert.get_cod_seq_lib(1)
            """,nativeQuery = true)
    public String getStudentCodNew();
}
