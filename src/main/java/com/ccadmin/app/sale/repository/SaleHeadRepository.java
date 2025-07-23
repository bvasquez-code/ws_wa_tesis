package com.ccadmin.app.sale.repository;

import com.ccadmin.app.sale.model.entity.SaleHeadEntity;
import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleHeadRepository extends JpaRepository<SaleHeadEntity,String>, CcAdminRepository<SaleHeadEntity,String> {

    @Query(value = """
            CALL db_store_01.get_cod_trx(:storeCod, 'sale_head')
            """,nativeQuery = true)
    public String getSaleCod(@Param("storeCod") String storeCod);

    @Override
    @Query(value = """
            select count(1) from sale_head sh
            left join client c on c.ClientCod = sh.ClientCod
            left join person p on p.PersonCod = c.PersonCod
            where
            sh.SaleCod = :id or ( ('' != :query and concat(sh.SaleCod,p.DocumentNum,p.Names,p.LastNames) like concat('%',:query,'%') ) or ( '' = :query ) )
            and sh.StoreCod = :storeCod
            """,nativeQuery = true)
    public int countByQueryTextStore(
              @Param("id") String id
            , @Param("query") String query
            , @Param("storeCod") String storeCod
    );

    @Override
    @Query(value = """
            select sh.* from sale_head sh
            left join client c on c.ClientCod = sh.ClientCod
            left join person p on p.PersonCod = c.PersonCod
            where
            sh.SaleCod = :id or ( ('' != :query and concat(sh.SaleCod,p.DocumentNum,p.Names,p.LastNames) like concat('%',:query,'%') ) or ( '' = :query ) )
            and sh.StoreCod = :storeCod
            order by sh.SaleCod desc
            limit :init,:limit
            """,nativeQuery = true)
    public List<SaleHeadEntity> findByQueryTextStore(
              @Param("id") String id
            , @Param("query") String query
            , @Param("storeCod") String storeCod
            , @Param("init") int init
            , @Param("limit") int limit
    );
}
