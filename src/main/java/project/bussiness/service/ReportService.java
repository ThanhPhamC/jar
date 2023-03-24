package project.bussiness.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ReportService {
    ResponseEntity<?> reportByAddress(Map<String,String> header );
}
