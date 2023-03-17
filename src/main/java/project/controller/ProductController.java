package project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bussiness.service.ProductService;
import project.model.dto.response.ProductResponse;
import project.model.shopMess.Message;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/product")
public class ProductController {
@Autowired private ProductService productService;
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
