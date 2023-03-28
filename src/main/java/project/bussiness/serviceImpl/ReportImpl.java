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
            List<Object[]> result = new ArrayList<>();
            List<AddressRevenue> responses = new ArrayList<>();
            List<Cart> carts = new ArrayList<>();
            if (reportTime.equals("day") || reportTime == "") {
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
                result = responses.stream()
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

            } else if (reportTime.equals("week")) {
                result = reportRepo.find_by_week_address(start.toLocalDate(), end.toLocalDate(), city, 4);
            } else if (reportTime.equals("month")) {
                result = reportRepo.find_by_month_address(start.toLocalDate(), end.toLocalDate(), city, 4);
            }
            if (header.get("export").equals("excel")) {
                export.setData(result);
                if (reportTime.equals("day")) {
                    AddressRevenue add = new AddressRevenue();
                    export.export(response, add);
                } else {
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
        Map<Integer, ProductReportByCatalog> result = detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getCatalog().getId() == catId)
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd -> new ProductReportByCatalog(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getPrice() * cd.getQuantity(),
                                cd.getQuantity(),
                                cd.getDiscount(),
                                cd.getProduct().getCatalog().getName(),
                                cd.getPrice() * cd.getQuantity() + cd.getDiscount() * cd.getQuantity()
                        ),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                ));
        return new ArrayList<>(result.values());
    }

    @Override
    public List<ProductReportByBrand> reportByBrand(int status, int bradId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, createDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        Map<Integer,ProductReportByBrand>result=detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getBrand().getId()==bradId)
                .collect(Collectors.toMap(cartDetail -> cartDetail.getProduct().getId(),
                        cartDetail -> new ProductReportByBrand(cartDetail.getProduct().getId(),
                                cartDetail.getProduct().getName(),
                                cartDetail.getQuantity(),
                                cartDetail.getQuantity()* cartDetail.getPrice()+cartDetail.getQuantity()* cartDetail.getDiscount(),
                                cartDetail.getDiscount(),
                                cartDetail.getQuantity()*cartDetail.getPrice(),
                                cartDetail.getProduct().getBrand().getName()),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                        ));
        return new ArrayList<>(result.values());
    }

    @Override
    public List<ProductReportByLocation> reportByLocation(int status, int locationId, LocalDateTime createDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, createDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(list);
        Map<Integer,ProductReportByLocation> result =detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getLocation().getId()==locationId)
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd ->new ProductReportByLocation(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getQuantity(),
                                cd.getQuantity()* cd.getPrice()+cd.getQuantity()* cd.getDiscount(),
                                cd.getDiscount(),
                                cd.getPrice()*cd.getQuantity(),
                                cd.getProduct().getLocation().getName()),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                        ));
        return new ArrayList<>(result.values());
    }

    @Override
    public List<ProductByCartStatusResponse> reportByCart(int status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> cartList = cartRepo.findCartByStatusAndCreatDateBetween(status, startDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(cartList);
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
    public List<ProductByCatalogByCartStt> reportProByCatalogCart(int status, int catId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> cartList = cartRepo.findCartByStatusAndCreatDateBetween(status, startDate, endDate);
        List<CartDetail> detailList = cartDetailRepo.findByCartIn(cartList);
        Map<Integer, ProductByCatalogByCartStt> result = detailList.stream()
                .filter(cartDetail -> cartDetail.getProduct().getCatalog().getId() == catId)
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),
                        cd -> new ProductByCatalogByCartStt(cd.getProduct().getId(),
                                cd.getProduct().getName(),
                                cd.getPrice() * cd.getQuantity(),
                                cd.getQuantity(),
                                cd.getDiscount(),
                                cd.getProduct().getCatalog().getName(),
                                cd.getPrice() * cd.getQuantity() + cd.getDiscount() * cd.getQuantity()
                        ),
                        (pr1, pr2) -> {
                            pr1.setQuantitySales(pr1.getQuantitySales() + pr2.getQuantitySales());
                            pr1.setRealRevenue(pr1.getRealRevenue() + pr2.getRealRevenue());
                            pr1.setRevenue(pr1.getRevenue() + pr2.getRevenue());
                            return pr1;
                        }
                ));
        return new ArrayList<>(result.values());
    }
    @Override
    public List<ProductByCartSttCancel> reportProByCartCancel(int status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> list = cartRepo.findCartByStatusAndCreatDateBetween(status, startDate, endDate);
        List<CartDetail> cartDetailList = cartDetailRepo.findByCartIn(list);
        Map<Integer, ProductByCartSttCancel> resultMap = cartDetailList.stream() // duyet mang detailList
                .collect(Collectors.toMap(cd -> cd.getProduct().getId(),//mỗi 1 phần tử detail thì chuyển giá trị qua product
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
}
