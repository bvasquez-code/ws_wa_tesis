package com.ccadmin.app.shared.service;

import com.ccadmin.app.shared.interfaceccadmin.CcAdminRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearchT;
import com.ccadmin.app.shared.model.dto.SearchDto;

public class SearchTService<T> {

    private CcAdminRepository ccAdminRepository;

    public SearchTService(CcAdminRepository ccAdminRepository)
    {
        this.ccAdminRepository = ccAdminRepository;
    }

    public ResponsePageSearchT <T>findAll(SearchDto search, int Limit)
    {
        search.setLimit(Limit);

        return executeFindAll(search);
    }

    public ResponsePageSearchT <T>findAll(SearchDto search)
    {
        return executeFindAll(search);
    }

    public ResponsePageSearchT <T>findAllStore(SearchDto search, int Limit)
    {
        search.setLimit(Limit);

        return executeFindAllStore(search);
    }

    public ResponsePageSearchT <T>findAllStore(SearchDto search)
    {
        return executeFindAllStore(search);
    }

    public ResponsePageSearchT <T>findAllUserCod(SearchDto search)
    {
        return executeFindAllUserCod(search);
    }
    private ResponsePageSearchT <T>executeFindAll(SearchDto search)
    {
        return new ResponsePageSearchT(
                this.ccAdminRepository.findByQueryText(search.Query,search.Query,search.Init,search.Limit),
                search.Page,
                search.Limit,
                this.ccAdminRepository.countByQueryText(search.Query,search.Query)
        );
    }

    private ResponsePageSearchT <T>executeFindAllStore(SearchDto search)
    {
        return new ResponsePageSearchT(
                this.ccAdminRepository.findByQueryTextStore(search.Query,search.Query,search.StoreCod,search.Init,search.Limit),
                search.Page,
                search.Limit,
                this.ccAdminRepository.countByQueryTextStore(search.Query,search.Query,search.StoreCod)
        );
    }

    private ResponsePageSearchT <T>executeFindAllUserCod(SearchDto search)
    {
        return new ResponsePageSearchT(
                this.ccAdminRepository.findByQueryTextUserCod(search.Query,search.Query,search.UserCod,search.Init,search.Limit),
                search.Page,
                search.Limit,
                this.ccAdminRepository.countByQueryTextUserCod(search.Query,search.Query,search.UserCod)
        );
    }
}
