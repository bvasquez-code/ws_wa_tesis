package com.ccadmin.app.product.service;

import com.ccadmin.app.product.model.entity.BrandEntity;
import com.ccadmin.app.product.repository.BrandRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearch;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchService;
import com.ccadmin.app.shared.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService extends SessionService {

    @Autowired
    private BrandRepository brandRepository;
    private SearchService searchService;

    public BrandEntity findById(String brandCod)
    {
        return this.brandRepository.findById(brandCod).get();
    }

    public ResponsePageSearch findAll(String Query, int Page)
    {
        this.searchService = new SearchService(this.brandRepository);
        SearchDto search = new SearchDto(Query,Page);
        ResponsePageSearch responsePage = this.searchService.findAll(search,10);
        return responsePage;
    }

    public ResponseWsDto findDataForm(String BrandCod)
    {
        ResponseWsDto rpt = new ResponseWsDto();

        if( BrandCod != null && BrandCod.length() > 0 )
        {
            rpt.AddResponseAdditional("brand",this.brandRepository.findById(BrandCod).get());
        }

        return rpt;
    }

    public BrandEntity save(BrandEntity brand)
    {
        brand.addSession(getUserCod(),!this.brandRepository.existsById(brand.BrandCod));

        return this.brandRepository.save(brand);
    }

}
