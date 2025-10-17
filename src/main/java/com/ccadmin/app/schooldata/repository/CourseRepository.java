package com.ccadmin.app.schooldata.repository;

import com.ccadmin.app.schooldata.model.entity.CourseEntity;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, String>, CcAdminRepository<CourseEntity, String> {

    @Override
    @Query(value = """
           select count(1)
           from data_courses c
           where (c.Course = :id or c.Course like %:query%)
           """, nativeQuery = true)
    int countByQueryText(@Param("id") String id, @Param("query") String query);

    @Override
    @Query(value = """
           select *
           from data_courses c
           where (c.Course = :id or c.Course like %:query%)
           order by c.CreationDate desc
           limit :init, :limit
           """, nativeQuery = true)
    List<CourseEntity> findByQueryText(@Param("id") String id, @Param("query") String query,
                                       @Param("init") int init, @Param("limit") int limit);

    @Query(value = """
           select *
           from data_courses c
           where c.Status = 'A'
           order by c.Course
           """, nativeQuery = true)
    List<CourseEntity> findAllActive();
}
