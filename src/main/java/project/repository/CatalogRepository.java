package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Catalog;
@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Integer> {
}
