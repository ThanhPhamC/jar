package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.ReportService;

import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/report")
@AllArgsConstructor
public class ReportController {
    private ReportService reportService;
        @GetMapping("/report_by_address")
    public ResponseEntity<?> reportByAddress(@RequestParam Map<String,String> header ){
        return reportService.reportByAddress(header);
    }
}
