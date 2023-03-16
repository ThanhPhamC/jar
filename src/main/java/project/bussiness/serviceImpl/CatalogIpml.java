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
import project.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CatalogIpml implements CatalogService {
    @Autowired
    CatalogRepository catalogRepo;
    @Autowired
    UserRepository userRepository;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Catalog> page = catalogRepo.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }
    @Override
    public CatalogResponse saveOrUpdate(CatalogRequest request) {
            Catalog input = mapRequestToPoJo(request);
            Catalog outPut=catalogRepo.save(input);
            CatalogResponse response= mapPoJoToResponse(outPut);
            return response;
    }
    @Override
    public boolean delete(Integer id) {
        try {
            catalogRepo.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Catalog> findAll() {
        return null;
    }

    @Override
    public Catalog findById(Integer id) {
        return catalogRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Catalog mapRequestToPoJo(CatalogRequest catalogRequest) {
        Catalog catalog = new Catalog();
        if (catalogRequest.getId()!=0){
            catalog.setId(catalogRequest.getId());
        }
        catalog.setName(catalogRequest.getName());
        catalog.setStatus(catalogRequest.getStatus());
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
        List<Catalog> catalogs= catalogRepo.findAll();
        Collections.sort(catalogs, Comparator.comparingInt(catalog -> catalog.getProductList().size()));
        List<Catalog> outPut= catalogs.subList(catalogs.size()-7,catalogs.size()-1);
        List<CatalogResponse> resout= (List<CatalogResponse>) outPut.stream().map(catalog -> {
        CatalogResponse response=  mapPoJoToResponse(catalog); return  response;});
        return resout;
    }
}
