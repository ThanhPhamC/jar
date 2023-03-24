package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import project.bussiness.service.ReportService;
import project.model.dto.response.AddressRevenue;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.CatalogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
