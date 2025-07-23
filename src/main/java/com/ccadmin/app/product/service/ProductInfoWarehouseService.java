package com.ccadmin.app.product.service;

import com.ccadmin.app.product.model.entity.ProductInfoWarehouseEntity;
import com.ccadmin.app.product.model.entity.id.ProductInfoWarehouseId;
import com.ccadmin.app.product.repository.ProductInfoWarehouseRepository;
import com.ccadmin.app.store.model.entity.WarehouseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoWarehouseService {

    @Autowired
    private ProductInfoWarehouseRepository productInfoWarehouseRepository;

    public ProductInfoWarehouseEntity findById(ProductInfoWarehouseId id)
    {
        return this.productInfoWarehouseRepository.findById(id).get();
    }

    public List<ProductInfoWarehouseEntity> saveAll(List<ProductInfoWarehouseEntity> list)
    {
        return this.productInfoWarehouseRepository.saveAll(list);
    }

    public ProductInfoWarehouseEntity save(ProductInfoWarehouseEntity productInfoWarehouse)
    {
        return this.productInfoWarehouseRepository.save(productInfoWarehouse);
    }

    public List<ProductInfoWarehouseEntity> findByStore(String StoreCod){
        return this.productInfoWarehouseRepository.findByStoreCod(StoreCod);
    }

    public List<ProductInfoWarehouseEntity> findInfoWarehouse(String StoreCod,String ProductCod){
        return this.productInfoWarehouseRepository.findInfoWarehouse(StoreCod,ProductCod);
    }
}
