package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.ProductService;
import project.model.dto.request.ProductFeatureRequest;
import project.model.dto.response.ProductResponse;
import project.model.shopMess.Message;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("getFeatureProduct")
    public ResponseEntity<?> getFeatureSlider(@RequestBody ProductFeatureRequest productFeatureRequest) {
        try {
            List<ProductResponse> responses = productService.getFeatureProduct(productFeatureRequest.getStartDate(), productFeatureRequest.getEndDate());
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/top_new_product")
    public ResponseEntity<?> topNewProduct(){
        try {
            List<ProductResponse> responses=productService.topNewProduct();
                if (responses.isEmpty()){
                    return ResponseEntity.badRequest().body(Message.ERROR_NULL);
                }else {
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

}
