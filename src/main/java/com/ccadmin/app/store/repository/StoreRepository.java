package com.ccadmin.app.store.repository;

import com.ccadmin.app.store.model.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<StoreEntity,String> {

    @Query(value = """
            select count(1)  from warehouse w where w.StoreCod = :StoreCod and Status = 'A'
            """,nativeQuery = true)
    public int countNumberWarehouse(@Param("StoreCod") String StoreCod);
}
