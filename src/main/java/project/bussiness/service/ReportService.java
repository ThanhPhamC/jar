package project.bussiness.service;

import org.springframework.http.ResponseEntity;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.dto.response.ProductReportByLocation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {
    ResponseEntity<?> reportByAddress(Map<String,String> header );
    List<ProductReportByCatalog> reportByCatalog(int status, int catId, LocalDateTime creDate, LocalDateTime endTime);
    List<ProductReportByBrand> reportByBrand(int status , int bradId, LocalDateTime createDate, LocalDateTime endDate);
    List<ProductReportByLocation> reportByLocation(int status,int locationId,LocalDateTime createDate,LocalDateTime endDate);
}
