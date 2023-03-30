package project.model.dto.response;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
public class Revenue {
    private String date;
    private float totalTax;
    private float totalShip;
    private float totalDiscount;
    private float revenue;
    private float realRevenue;
    private int numberOder;

    public Revenue(String date, float totalTax, float totalShip, float totalDiscount, float revenue,float realRevenue,int numberOder) {
        this.date = date;
        this.totalTax = totalTax;
        this.totalShip = totalShip;
        this.totalDiscount = totalDiscount;
        this.revenue = revenue;
        this.realRevenue=realRevenue;
        this.numberOder=numberOder;
    }

    public Revenue() {
    }
}
