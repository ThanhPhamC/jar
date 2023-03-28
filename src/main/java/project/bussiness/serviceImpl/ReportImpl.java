package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.ReportService;
import project.model.dto.response.AddressRevenue;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.dto.response.ProductReportByLocation;
import project.model.dto.response.*;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.ExcelExport;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.ReportRepository;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {
    private CartRepository cartRepo;
    private CartDetailRepository cartDetailRepo;
    private ExcelExport export;
    private ReportRepository reportRepo;

    @Override
    public ResponseEntity<?> reportByAddress(Map<String, String> header, HttpServletResponse response) {
        try {
            LocalDateTime start = LocalDateTime.parse(header.get("start"));
            LocalDateTime end = LocalDateTime.parse(header.get("end"));
            long daysBetween = ChronoUnit.DAYS.between(start, end);
            String reportTime = header.get("reportTime");
            String city = header.get("value");
            List<Object[]> result= new ArrayList<>();
            List<AddressRevenue> responses = new ArrayList<>();
            List<Cart> carts = new ArrayList<>();
            if (reportTime.equals("day")||reportTime=="") {
                carts = cartRepo.findByStatusAndCityAndCreatDateBetween(Constants.CART_STATUS_DONE, city, start, end);
                for (int i = 0; i <= daysBetween; i++) {
                    AddressRevenue add = new AddressRevenue();
                    add.setId(i + 1);
                    add.setSaleDate(start.plusDays(i).toLocalDate());
                    for (Cart cart : carts) {
                        LocalDate date = cart.getCreatDate().toLocalDate();
                        if (date.equals(add.getSaleDate())) {
                            add.setDiscount(add.getDiscount() + cart.getDiscount());
                            add.setRevenue(add.getRevenue() + cart.getTotal() + cart.getDiscount());
                            add.setTax(add.getTax() + cart.getTax());
                            add.setShip(add.getShip() + cart.getShipping());
                            add.setCountOder(add.getCountOder() + 1);
                        }
                    }
                    add.setTotalRevenue(add.getRevenue() + add.getTax() + add.getShip() - add.getDiscount());
                    responses.add(add);
                }
                result= responses.stream()
                        .map(revenue -> new Object[]{
                                revenue.getId(),
                                revenue.getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                revenue.getRevenue(),
                                revenue.getCountOder(),
                                revenue.getShip(),
                                revenue.getTax(),
                                revenue.getDiscount(),
                                revenue.getTotalRevenue()
                        })
                        .collect(Collectors.toList());

            }else if (reportTime.equals("week")){
            result = reportRepo.find_by_week_address(start.toLocalDate(), end.toLocalDate(), city, 4);
            }else if (reportTime.equals("month")){
             result = reportRepo.find_by_month_address(start.toLocalDate(), end.toLocalDate(), city, 4);
            }
            if (header.get("export").equals("excel")) {
                export.setData(result);
                if (reportTime.equals("day")){
                    AddressRevenue add = new AddressRevenue();
                    export.export(response, add);
                }else {
                    RevenueAddress revenueAddress = new RevenueAddress();
                    export.export(response, revenueAddress);
                }
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }



    @Override
    public List<ProductReportByCatalog> reportByCatalog(int status, int catId, LocalDateTime creDate, LocalDateTime endTime) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, creDate, endTime);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        List<ProductReportByCatalog> listProDto = new ArrayList<>();
        for (CartDetail ca : detailList) {
            boolean check = false;
            if (ca.getProduct().getCatalog().getId() == catId && listProDto.size() != 0) {
                for (ProductReportByCatalog proResponse : listProDto) {
                    if (proResponse.getId() == ca.getProduct().getId()) {
                        proResponse.setQuantitySales(proResponse.getQuantitySales() + ca.getQuantity());
                        proResponse.setRevenue(proResponse.getRevenue() + ca.getPrice());
                        proResponse.setRealRevenue(proResponse.getRevenue() + proResponse.getDiscount());
                    } else {
                        check = true;
                    }
                }
            } else if (ca.getProduct().getCatalog().getId() == catId) {
                check = true;
            }
            if (check) {
                ProductReportByCatalog pr1 = new ProductReportByCatalog();
                pr1.setName(ca.getProduct().getName());
                pr1.setQuantitySales(ca.getQuantity());
                pr1.setRevenue(ca.getPrice() * pr1.getQuantitySales());
                pr1.setId(ca.getProduct().getId());
                pr1.setStatus(ca.getStatus());
                pr1.setCatalogName(ca.getProduct().getCatalog().getName());
                pr1.setRealRevenue(pr1.getRevenue() - pr1.getDiscount());
                listProDto.add(pr1);
            }
        }
        return listProDto;
    }

    @Override
    public List<ProductReportByBrand> reportByBrand(int status, int bradId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, createDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        List<ProductReportByBrand> listProDto = new ArrayList<>();
        for (CartDetail ca : detailList) {
            boolean check = false;
            if (ca.getProduct().getBrand().getId() == bradId && listProDto.size() != 0) {
                for (ProductReportByBrand proResponse : listProDto) {
                    if (proResponse.getId() == ca.getProduct().getId()) {
                        proResponse.setQuantitySales(proResponse.getQuantitySales() + ca.getQuantity());
                        proResponse.setRevenue(proResponse.getRevenue() + ca.getPrice());
                        proResponse.setRealRevenue(proResponse.getRevenue() + proResponse.getDiscount());
                    } else {
                        check = true;
                    }
                }
            } else if (ca.getProduct().getBrand().getId() == bradId) {
                check = true;
            }
            if (check) {
                ProductReportByBrand pr1 = new ProductReportByBrand();
                pr1.setName(ca.getProduct().getName());
                pr1.setQuantitySales(ca.getQuantity());
                pr1.setRevenue(ca.getPrice() * pr1.getQuantitySales());
                pr1.setId(ca.getProduct().getId());
                pr1.setStatus(ca.getStatus());
                pr1.setBrandName(ca.getProduct().getBrand().getName());
                pr1.setRealRevenue(pr1.getRevenue() - pr1.getDiscount());
                listProDto.add(pr1);
            }
        }
        return listProDto;
    }

    @Override
    public List<ProductReportByLocation> reportByLocation(int status, int locationId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list =cartRepo.findCartByStatusAndCreatDateBetween(status,createDate,endDate);
        List<CartDetail> detailList =cartDetailRepo.findByCartIn(list);
        List<ProductReportByLocation>listProDto= new ArrayList<>();
        for (CartDetail ca:detailList) {
            boolean check   = false;
            if (ca.getProduct().getLocation().getId()==locationId&&listProDto.size()!=0){
                for (ProductReportByLocation proResponse:listProDto) {
                    if (proResponse.getId()==ca.getProduct().getId()){
                        proResponse.setQuantitySales(proResponse.getQuantitySales()+ca.getQuantity());
                        proResponse.setRevenue(proResponse.getRevenue()+ca.getPrice());
                        proResponse.setRealRevenue(proResponse.getRevenue()+ proResponse.getDiscount());
                    }else {
                        check=true;
                    }
                }
            }else if (ca.getProduct().getLocation().getId()==locationId){
                check=true;
            }
            if (check){
                ProductReportByLocation pr1 =new ProductReportByLocation();
                pr1.setName(ca.getProduct().getName());
                pr1.setQuantitySales(ca.getQuantity());
                pr1.setRevenue(ca.getPrice()*pr1.getQuantitySales());
                pr1.setId(ca.getProduct().getId());
                pr1.setStatus(ca.getStatus());
                pr1.setLocationName(ca.getProduct().getLocation().getName());
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
        Map<Integer, ProductByCartStatusResponse> resultMap = detailList.stream() // duyet mang detailList
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),//mỗi 1 phần tử detail thì chuyeenr giá trị qua product
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
    }
    @Override
    public List<ProductByCatalogByCartStt> reportProByCatalogCart(int status,int catId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart>cartList = cartRepo.findCartByStatusAndCreatDateBetween(status,startDate,endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(cartList);
        List<ProductByCatalogByCartStt> listProDto = new ArrayList<>();
        for (CartDetail ca : detailList) {
            boolean check = false;
            if (ca.getProduct().getCatalog().getId() == catId && listProDto.size() != 0) {
                for (ProductByCatalogByCartStt proResponse : listProDto) {
                    if (proResponse.getId() == ca.getProduct().getId()) {
                        proResponse.setQuantity(proResponse.getQuantity() + ca.getQuantity());
                    } else {
                        check = true;
                    }
                }
            } else if (ca.getProduct().getCatalog().getId() == catId) {
                check = true;
            }
            if (check) {
                ProductByCatalogByCartStt pr1 = new ProductByCatalogByCartStt();
                pr1.setName(ca.getProduct().getName());
                pr1.setQuantity(ca.getQuantity());
                pr1.setId(ca.getProduct().getId());
                pr1.setStatus(ca.getStatus());
                pr1.setCatalogName(ca.getProduct().getCatalog().getName());
                pr1.setExportPrice(ca.getProduct().getExportPrice());
                listProDto.add(pr1);
            }
        }
        return listProDto;
    }
    @Override
    public List<ProductByCartSttCancel> reportProByCartCancel(int status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart>list=cartRepo.findCartByStatusAndCreatDateBetween(status,startDate,endDate);
        List<CartDetail> cartDetailList=cartDetailRepo.findByCartIn(list);
        Map<Integer, ProductByCartSttCancel> resultMap = cartDetailList.stream() // duyet mang detailList
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),//mỗi 1 phần tử detail thì chuyeenr giá trị qua product
                        cd -> new ProductByCartSttCancel(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getProduct().getExportPrice(),
                                cd.getQuantity(),
                                cd.getProduct().getDiscount(),
                                cd.getProduct().getCatalog().getName()),
                        (pr1, pr2) -> {
                            pr1.setQuantity(pr1.getQuantity() + pr2.getQuantity());
                            return pr1;
                        }
                ));
        return new ArrayList<>(resultMap.values());
    }
    @Override
    public ResponseEntity<?> reportRevenueAll(Map<String, String> header, HttpServletResponse response) {
        try {
            LocalDateTime start = LocalDateTime.parse(header.get("start"));
            LocalDateTime end = LocalDateTime.parse(header.get("end"));
            String reportTime = header.get("reportTime");
            List<Object[]> objects= new ArrayList<>();
            switch (reportTime){
                case Constants.DAY:
                    objects=reportRepo.find_by_day_total(start.toLocalDate(),end.toLocalDate(),Constants.CART_STATUS_DONE);
                    break;
                case Constants.WEEK:
                    objects=reportRepo.find_by_week_total(start.toLocalDate(),end.toLocalDate(),Constants.CART_STATUS_DONE);
                    break;
                case Constants.MONTH:
                    objects=reportRepo.find_by_month_total(start.toLocalDate(),end.toLocalDate(),Constants.CART_STATUS_DONE);
                    break;
                default: break;
            }
            if (header.get("export").equals("excel")){
                Revenue revenue = new Revenue();
                export.setData(objects);
                export.export(response,revenue);
            }
            List<Revenue> result = objects.stream().map(obj -> {
                String date = obj[0].toString();
                float totalTax = Float.parseFloat(obj[1].toString()) ;
                float totalShip = Float.parseFloat(obj[2].toString());
                float totalDiscount = Float.parseFloat(obj[3].toString());
                float total = Float.parseFloat(obj[4].toString());
                return new Revenue(date, totalTax, totalShip,totalDiscount,total);
            }).collect(Collectors.toList());
                return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
