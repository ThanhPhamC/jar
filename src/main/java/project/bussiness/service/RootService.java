package project.bussiness.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RootService<T,V,H,K> {
    Map<String,Object> getPagingAndSort(Pageable pageable);
    K saveOrUpdate(T t);
    boolean delete (V id);
    List<T> findAll();
    T findById(V id);
    Map<String,Object> findByName(String name,Pageable pageable);
    T mapRequestToPoJo(H h);
    K mapPoJoToResponse(T t);
}