package project.bussiness.service;

import project.model.dto.request.CatalogRequest;
import project.model.dto.response.CatalogResponse;
import project.model.entity.Catalog;

public interface CatalogService extends RootService<Catalog,Integer, CatalogRequest, CatalogResponse>{

}
