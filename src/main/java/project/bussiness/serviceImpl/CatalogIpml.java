package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bussiness.service.CatalogService;
import project.model.utility.Utility;
import project.model.dto.request.CatalogRequest;
import project.model.dto.response.CatalogResponse;
import project.model.entity.Catalog;
import project.repository.CatalogRepository;

import java.util.List;
import java.util.Map;

@Service
public class CatalogIpml implements CatalogService {
    @Autowired
    CatalogRepository catalogRepo;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Catalog> page = catalogRepo.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public CatalogResponse saveOrUpdate(Catalog catalog) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Catalog> findAll() {
        return null;
    }

    @Override
    public Catalog findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Catalog mapRequestToPoJo(CatalogRequest catalogRequest) {
        return null;
    }

    @Override
    public CatalogResponse mapPoJoToResponse(Catalog catalog) {
        CatalogResponse response = new CatalogResponse();
        response.setCatalogId(catalog.getId());
        response.setCatalogName(catalog.getName());
        response.setCatalogStatus(catalog.getStatus());
        return response;
    }
}
