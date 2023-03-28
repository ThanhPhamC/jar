package project.model.dto.response;

import lombok.Data;
@Data
public class Revenue {
    private String date;
    private float totalTax;
    private float totalShip;
    private float totalDiscount;
    private float total;

    public Revenue(String date, float totalTax, float totalShip, float totalDiscount, float total) {
        this.date=date;
        this.totalTax=totalTax;
        this.totalShip=totalShip;
        this.totalDiscount=totalDiscount;
        this.total=total;

    }

    public Revenue() {
    }
}
