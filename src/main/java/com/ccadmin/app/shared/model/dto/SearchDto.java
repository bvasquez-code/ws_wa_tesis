package com.ccadmin.app.shared.model.dto;

public class SearchDto {

    public String Query;
    public int Page;

    public String StoreCod;
    public int Limit;
    public int Init;
    public String UserCod;

    public SearchDto()
    {
        setLimit(10);
    }

    public SearchDto(String Query,int Page)
    {
        this.Query = Query;
        this.Page = Page;
        setLimit(10);
    }
    public SearchDto(String Query,int Page,String StoreCod)
    {
        this.Query = Query;
        this.Page = Page;
        this.StoreCod =  StoreCod;
        setLimit(10);
    }

    public SearchDto(String Query,int Page,String UserCod,String StoreCod)
    {
        this.Query = Query;
        this.Page = Page;
        this.UserCod =  UserCod;
        this.StoreCod =  StoreCod;
        setLimit(10);
    }

    public void setLimit(int Limit)
    {
        this.Limit = Limit;
        this.Init = (Page - 1) * this.Limit;
    }

}
