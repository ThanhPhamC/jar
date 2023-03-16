package project.controller;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CatalogService;
import project.model.utility.Utility;
import project.model.shopMess.Message;
import java.util.Map;

@RestController
@CrossOrigin("https://localhost8080")
@RequestMapping("/api/v1/catalog")
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    @GetMapping
    public ResponseEntity<?> get_paging_and_sort(@RequestParam Map<String, String> headers) {
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String, Object> result = catalogService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
}
