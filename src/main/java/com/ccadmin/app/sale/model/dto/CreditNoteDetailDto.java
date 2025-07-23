package com.ccadmin.app.sale.model.dto;

import com.ccadmin.app.client.model.entity.ClientEntity;
import com.ccadmin.app.sale.model.entity.CreditNoteDetEntity;
import com.ccadmin.app.sale.model.entity.CreditNoteDocumentEntity;
import com.ccadmin.app.sale.model.entity.CreditNoteHeadEntity;

import java.util.List;

public class CreditNoteDetailDto {

    public ClientEntity Client;

    public CreditNoteHeadEntity Headboard;
    public CreditNoteDocumentEntity Document;

    public List<CreditNoteDetDto> DetailList;

}
