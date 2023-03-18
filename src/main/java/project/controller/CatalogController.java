package project.controller;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CatalogService;
import project.model.dto.request.CatalogRequest;
import project.model.dto.request.ProductFeatureRequest;
import project.model.dto.response.CatalogResponse;
import project.model.utility.Utility;
import project.model.shopMess.Message;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/catalog")
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    @GetMapping
    public ResponseEntity<?> get_paging_and_sort(@RequestParam Map<String,String> headers) {
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String, Object> result = catalogService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get_feature_catalog")
    public ResponseEntity<?> getFeatureCatalog(){
        try {
            List<CatalogResponse> result= catalogService.getListFeatured();
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping
    public ResponseEntity<?>creatNewCatalog(@RequestBody CatalogRequest request){
        try {
            CatalogResponse result= catalogService.saveOrUpdate(request);
            return  new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/get_feature_catalog_for_screen_2")
    public ResponseEntity<?> getFeatureCatalogForScreen2(@RequestBody ProductFeatureRequest productFeatureRequest){
        try {
            List<CatalogResponse> result= catalogService.getFeatureCatalogForScreen2(productFeatureRequest.getStartDate(),productFeatureRequest.getEndDate(), productFeatureRequest.getSize());
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }

}
