package com.ccadmin.app.product.service;

import com.ccadmin.app.product.model.entity.CategoryEntity;
import com.ccadmin.app.product.repository.CategoryRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearch;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchService;
import com.ccadmin.app.shared.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends SessionService {
    @Autowired
    private CategoryRepository categoryRepository;
    private SearchService searchService;


    public ResponsePageSearch findAll(String Query, int Page)
    {
        this.searchService = new SearchService(this.categoryRepository);
        SearchDto search = new SearchDto(Query,Page);
        ResponsePageSearch responsePage = this.searchService.findAll(search,10);
        return responsePage;
    }

    public ResponseWsDto findDataForm(String CategoryCod)
    {
        ResponseWsDto rpt = new ResponseWsDto();

        if( CategoryCod != null && CategoryCod.length() > 0 )
        {
            rpt.AddResponseAdditional("category",this.categoryRepository.findById(CategoryCod).get());
        }
        rpt.AddResponseAdditional("categoryDadList",this.categoryRepository.findAllActiveDad());

        return rpt;
    }

    public CategoryEntity save(CategoryEntity category){

        category.addSession(getUserCod(),!this.categoryRepository.existsById(category.CategoryCod));

        return this.categoryRepository.save(category);
    }
}
