package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.BrandService;
import project.model.dto.request.ProductFeatureRequest;
import project.model.dto.response.BrandResponse;
import project.model.shopMess.Message;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/brand")
@AllArgsConstructor
public class BrandController {
    private BrandService brandService;

    @GetMapping("/get_feature_brand")
    public ResponseEntity<?> getFeatureBrand(@RequestBody ProductFeatureRequest productFeatureRequest){
        try {
            List<BrandResponse> responses = brandService.getFeatureBrand(productFeatureRequest.getStartDate(), productFeatureRequest.getEndDate(), productFeatureRequest.getSize());
            return new ResponseEntity<>(responses,HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
