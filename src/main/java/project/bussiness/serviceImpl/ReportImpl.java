package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.ReportService;
import project.model.dto.response.AddressRevenue;
import project.model.dto.response.ProductByCartStatusResponse;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.shopMess.Message;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {
    private CartRepository cartRepo;
    private CartDetailRepository cartDetailRepo;


    @Override
    public ResponseEntity<?> reportByAddress(Map<String, String> header) {
        try {
            LocalDateTime start = LocalDateTime.parse(header.get("start"));
            LocalDateTime end = LocalDateTime.parse(header.get("end"));
            long daysBetween = ChronoUnit.DAYS.between(start, end);
            String reportBy = header.get("reportBy");
            String city= header.get("value");
            List<Cart> carts= new ArrayList<>();
            if (reportBy.equals("day")){
                List<Cart>carts1=cartRepo.findByCreatDateBetween(start,end);
                 carts = cartRepo.findByStatusAndCityAndCreatDateBetween(4, city, start, end);
            }
            List<AddressRevenue> responses= new ArrayList<>();
            for (int i = 0; i <=daysBetween ; i++) {
                AddressRevenue add = new AddressRevenue();
                add.setId(i+1);
                add.setSaleDate(start.plusDays(i).toLocalDate());
                for (Cart cart:carts){
                    LocalDate date=cart.getCreatDate().toLocalDate();
                    if (date.equals(add.getSaleDate())){
                        add.setDiscount(add.getDiscount()+ cart.getDiscount());
                        add.setRevenue(add.getRevenue()+cart.getTotal()+cart.getDiscount());
                        add.setTax(add.getTax()+ cart.getTax());
                        add.setShip(add.getShip()+cart.getShipping());
                        add.setCountOder(add.getCountOder()+1);
                    }
                }
                add.setTotalRevenue(add.getRevenue()+ add.getTax()+ add.getShip()-add.getDiscount());
                responses.add(add);
            }
            return new ResponseEntity<>(responses, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public  List<ProductReportByCatalog> reportByCatalog(int status, int catId, LocalDateTime creDate, LocalDateTime endTime) {
        List<Cart> list =cartRepo.findCartByStatusAndCreatDateBetween(status,creDate,endTime);
        List<CartDetail> detailList =cartDetailRepo.findByCartIn(list);
        List<ProductReportByCatalog>listProDto= new ArrayList<>();
        for (CartDetail ca:detailList) {
            boolean check   = false;
            if (ca.getProduct().getCatalog().getId()==catId&&listProDto.size()!=0){
                for (ProductReportByCatalog proResponse:listProDto) {
                    if (proResponse.getId()==ca.getProduct().getId()){
                        proResponse.setQuantitySales(proResponse.getQuantitySales()+ca.getQuantity());
                        proResponse.setRevenue(proResponse.getRevenue()+ca.getPrice());
                        proResponse.setRealRevenue(proResponse.getRevenue()+ proResponse.getDiscount());
                    }else {
                        check=true;
                    }
                }
            }else if (ca.getProduct().getCatalog().getId()==catId){
                check=true;
            }
            if (check){
                ProductReportByCatalog pr1 =new ProductReportByCatalog();
                pr1.setName(ca.getProduct().getName());
                pr1.setQuantitySales(ca.getQuantity());
                pr1.setRevenue(ca.getPrice()*pr1.getQuantitySales());
                pr1.setId(ca.getProduct().getId());
                pr1.setStatus(ca.getStatus());
                pr1.setCatalogName(ca.getProduct().getCatalog().getName());
                pr1.setRealRevenue(pr1.getRevenue()-pr1.getDiscount());
                listProDto.add(pr1);
            }
        }
        return listProDto;
    }
    @Override
    public List<ProductReportByBrand> reportByBrand(int status, int bradId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list =cartRepo.findCartByStatusAndCreatDateBetween(status,createDate,endDate);
        List<CartDetail> detailList =cartDetailRepo.findByCartIn(list);
        List<ProductReportByBrand>listProDto= new ArrayList<>();
        for (CartDetail ca:detailList) {
            boolean check   = false;
            if (ca.getProduct().getBrand().getId()==bradId&&listProDto.size()!=0){
                for (ProductReportByBrand proResponse:listProDto) {
                    if (proResponse.getId()==ca.getProduct().getId()){
                        proResponse.setQuantitySales(proResponse.getQuantitySales()+ca.getQuantity());
                        proResponse.setRevenue(proResponse.getRevenue()+ca.getPrice());
                        proResponse.setRealRevenue(proResponse.getRevenue()+ proResponse.getDiscount());
                    }else {
                        check=true;
                    }
                }
            }else if (ca.getProduct().getBrand().getId()==bradId){
                check=true;
            }
            if (check){
                ProductReportByBrand pr1 =new ProductReportByBrand();
                pr1.setName(ca.getProduct().getName());
                pr1.setQuantitySales(ca.getQuantity());
                pr1.setRevenue(ca.getPrice()*pr1.getQuantitySales());
                pr1.setId(ca.getProduct().getId());
                pr1.setStatus(ca.getStatus());
                pr1.setBrandName(ca.getProduct().getBrand().getName());
                pr1.setRealRevenue(pr1.getRevenue()-pr1.getDiscount());
                listProDto.add(pr1);
            }
        }
        return listProDto;
    }

    @Override
    public List<ProductByCartStatusResponse> reportByCart(int status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart>cartList=cartRepo.findCartByStatusAndCreatDateBetween(status,startDate,endDate);
        List<CartDetail>detailList=cartDetailRepo.findByCartIn(cartList);
        Map<Integer, ProductByCartStatusResponse> resultMap = detailList.stream()
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd -> new ProductByCartStatusResponse(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getProduct().getExportPrice(),
                                cd.getQuantity(),
                                cd.getProduct().getDiscount()),
                        (pr1, pr2) -> {
                            pr1.setQuantity(pr1.getQuantity() + pr2.getQuantity());
                            return pr1;
                        }
                ));
        return new ArrayList<>(resultMap.values());
}}
