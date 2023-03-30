package project.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AddressRevenue {
    private String date;
    private float totalTax;
    private float totalShip;
    private float totalDiscount;
    private float revenue;
    private float realRevenue;
    private int numberOder;
   private String city;
    public AddressRevenue(String date, float totalTax, float totalShip, float totalDiscount, float revenue,float realRevenue,int numberOder,String city) {
        this.date=date;
        this.totalTax=totalTax;
        this.totalShip=totalShip;
        this.totalDiscount=totalDiscount;
        this.revenue=revenue;
        this.realRevenue=realRevenue;
        this.numberOder=numberOder;
        this.city=city;
    }
    public AddressRevenue() {
    }
}
