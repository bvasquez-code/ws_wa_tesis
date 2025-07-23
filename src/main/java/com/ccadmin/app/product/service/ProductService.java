package com.ccadmin.app.product.service;

import com.ccadmin.app.product.model.dto.ProductInfoDto;
import com.ccadmin.app.product.model.dto.ProductRegisterDto;
import com.ccadmin.app.product.model.dto.ProductRegisterMassiveDto;
import com.ccadmin.app.product.model.entity.ProductBarcodeEntity;
import com.ccadmin.app.product.model.entity.ProductEntity;
import com.ccadmin.app.product.model.entity.ProductPictureEntity;
import com.ccadmin.app.product.model.entity.ProductVariantEntity;
import com.ccadmin.app.product.model.entity.id.ProductPictureID;
import com.ccadmin.app.product.repository.*;
import com.ccadmin.app.shared.model.dto.ResponsePageSearch;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchService;
import com.ccadmin.app.shared.service.SessionService;
import com.ccadmin.app.system.shared.AppFileShared;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService extends SessionService {

    public static Logger log = LogManager.getLogger(SessionService.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductConfigRepository productConfigRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private ProductInfoWarehouseRepository productInfoWarehouseRepository;
    @Autowired
    private ProductPictureRepository productPictureRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductBarcodeRepository productBarcodeRepository;
    @Autowired
    private AppFileShared appFileShared;


    private SearchService searchService;


    public ResponsePageSearch findAll(String Query, int Page)
    {
        if(Query!=null && !Query.isEmpty()){
            Optional<ProductBarcodeEntity> productBarcodeOp = this.productBarcodeRepository.findById(Query);
            String ProductCod = (productBarcodeOp.isPresent()) ? productBarcodeOp.get().ProductCod : Query;
            Optional<ProductEntity> productOp = this.productRepository.findById(ProductCod);

            if(productOp.isPresent()){
                List<ProductEntity> ProductList = new ArrayList<>();
                ProductList.add(productOp.get());
                return new ResponsePageSearch(
                        ProductList,1,10,1
                );
            }
        }
        this.searchService = new SearchService(this.productRepository);
        SearchDto search = new SearchDto(Query,Page);
        ResponsePageSearch responsePage = this.searchService.findAll(search,10);
        return responsePage;
    }

    public ProductEntity findById(String ProductCod)
    {
        return this.productRepository.findById(ProductCod).get();
    }

    public ProductInfoDto findDetailById(String ProductCod,String StoreCod)
    {
        ProductInfoDto productInfoDto = new ProductInfoDto();

        productInfoDto.Product = productRepository.findById(ProductCod).get();
        productInfoDto.Config = productConfigRepository.findById(ProductCod).get();
        productInfoDto.VariantList = productVariantRepository.findAllVariantProduct(ProductCod);
        productInfoDto.InfoList = productInfoRepository.findInfoStore(ProductCod,StoreCod);
        productInfoDto.InfoWarehouseList = productInfoWarehouseRepository.findInfoWarehouse(StoreCod,ProductCod);
        productInfoDto.Picture = productPictureRepository.findPrincipal(ProductCod);
        return productInfoDto;
    }


    public ResponseWsDto findDataForm(String ProductCod) {

        ResponseWsDto rpt = new ResponseWsDto();

        if( ProductCod != null && ProductCod.length() > 0 )
        {
            ProductRegisterDto productRegister = new ProductRegisterDto();
            productRegister.product = this.findById(ProductCod);
            productRegister.config = this.productConfigRepository.findById(ProductCod).get();
            productRegister.pictureList = this.productPictureRepository.findAllByProductCod(ProductCod);

            for(var image : productRegister.pictureList){
                image.appFile = this.appFileShared.findById(image.FileCod);
            }

            rpt.AddResponseAdditional("product",productRegister);
        }
        rpt.AddResponseAdditional("brandList",this.brandRepository.findAllActive());
        rpt.AddResponseAdditional("categoryList",this.categoryRepository.findAllActiveNoDad());

        return rpt;
    }

    public ProductEntity findByBarCode(String BarCode){

        Optional<ProductBarcodeEntity> productBarcodeOp = this.productBarcodeRepository.findById(BarCode);

        String ProductCod = (productBarcodeOp.isPresent()) ? productBarcodeOp.get().ProductCod : BarCode;

        return findById(ProductCod);
    }

    public List<ProductEntity> findAll(){
        return this.productRepository.findAll();
    }

    public List<ProductEntity> findAllById(List<String> ProductCodList){
        return this.productRepository.findAllById(ProductCodList);
    }

    public List<ProductEntity> findByBrandCod(String BrandCod){
        return this.productRepository.findByBrandCod(BrandCod);
    }
    public List<ProductEntity> findByCategoryCod(String CategoryCod){
        return this.productRepository.findByCategoryCod(CategoryCod);
    }
}
