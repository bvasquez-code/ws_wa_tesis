package com.ccadmin.app.sale.repository;

import com.ccadmin.app.sale.model.entity.SalePaymentEntity;
import com.ccadmin.app.sale.model.entity.id.SalePaymentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SalePaymentRepository extends JpaRepository<SalePaymentEntity, SalePaymentID> {

    @Query( value = """
            select IFNULL(sum(sp.NumAmountPaid),0) as NumAmountPaid from sale_payments sp where sp.SaleCod = :SaleCod and sp.Status = 'A'
            """, nativeQuery = true)
    public BigDecimal findTotalPayment(String SaleCod);

    @Query( value = """
            select count(1) from sale_payments sp where sp.SaleCod = :SaleCod
            """, nativeQuery = true)
    public int countTotalPayment(String SaleCod);

    @Query( value = """
            select sp.* from sale_payments sp where sp.SaleCod = :SaleCod
            """, nativeQuery = true)
    public List<SalePaymentEntity> findBySaleCod(@Param("SaleCod") String SaleCod);

}
