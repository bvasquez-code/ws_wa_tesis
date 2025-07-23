package com.ccadmin.app.product.repository;

import com.ccadmin.app.product.model.entity.ProductBarcodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBarcodeRepository extends JpaRepository<ProductBarcodeEntity,String> {
}
