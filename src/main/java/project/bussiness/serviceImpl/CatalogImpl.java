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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CatalogImpl implements CatalogService {
    @Autowired
    CatalogRepository catalogRepo;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Catalog> page = catalogRepo.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public CatalogResponse saveOrUpdate(CatalogRequest catalogRequest) {
        return null;
    }

    @Override
    public CatalogResponse update(Integer id, CatalogRequest catalogRequest) {
        return null;
    }


    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Catalog> findAll() {
        List<Catalog> responses = catalogRepo.findAll();
        return responses;
    }

    @Override
    public List<CatalogResponse> getAllForClient() {
        List<CatalogResponse> responses = catalogRepo.findAll().stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        return responses;
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
        response.setId(catalog.getId());
        response.setName(catalog.getName());
        response.setStatus(catalog.getStatus());
        return response;
    }
    @Override
    public List<CatalogResponse> getListFeatured() {
        List<CatalogResponse> responses = catalogRepo.findAll().stream()
                .sorted(Comparator.comparingInt(catalog -> catalog.getProductList().size()))
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        List<CatalogResponse> result= responses.stream()
                .skip(Math.max(0, responses.size() - 6))
                .collect(Collectors.toList());
        return result;
    }
}
