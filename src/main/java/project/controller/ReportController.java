package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CartService;
import project.bussiness.service.ReportService;
import project.model.dto.response.ProductByCartStatusResponse;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.shopMess.Message;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/report")
@AllArgsConstructor
public class ReportController {
    private ReportService reportService;
    private CartService cartService;
        @GetMapping("/report_by_address")
    public ResponseEntity<?> reportByAddress(@RequestParam Map<String, String> header, HttpServletResponse response ){
        return reportService.reportByAddress(header,response);
    }
    @GetMapping("/proDuctByCatalog")
    public ResponseEntity<?>finProducByCatalog(@RequestParam int id,@RequestParam String startDate,@RequestParam String endDate){
        try {
            LocalDateTime start=LocalDateTime.parse(startDate);
            LocalDateTime end=LocalDateTime.parse(endDate);
            List<ProductReportByCatalog> list =  reportService.reportByCatalog(4,id,start,end);
            return  new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/productByBrand")
    public ResponseEntity<?>finProductByBrand(@RequestParam int id,@RequestParam String startDate,@RequestParam String endDate){
        try {
            LocalDateTime start=LocalDateTime.parse(startDate);
            LocalDateTime end=LocalDateTime.parse(endDate);
            List<ProductReportByBrand> list =  reportService.reportByBrand(4,id,start,end);
            return  new ResponseEntity<>(list,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/productByCartStatus")
    public ResponseEntity<?>productByCartStatus(@RequestParam String startDate,String endDate){
            try {
                LocalDateTime start = LocalDateTime.parse(startDate);
                LocalDateTime end =LocalDateTime.parse(endDate);
                List<ProductByCartStatusResponse> list =reportService.reportByCart(0,start,end);
                return new ResponseEntity<>(list,HttpStatus.OK);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(Message.ERROR_400);
            }
    }
}
