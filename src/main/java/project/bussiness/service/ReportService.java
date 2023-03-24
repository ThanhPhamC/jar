package project.bussiness.service;

import org.springframework.http.ResponseEntity;
import project.model.dto.response.ProductReportByCatalog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {
    ResponseEntity<?> reportByAddress(Map<String,String> header );
    List<ProductReportByCatalog> findCartByStatusAndCreatDateBetween(int status, int catId, LocalDateTime creDate, LocalDateTime endTime);

}
