package project.model.dto.response;

import lombok.Data;

@Data
public class TopProductByRevenue extends RootResponse{
    private int quantitySales;
    private float revenue;
    private float realRevenue;
    private  String catalogName;

    public TopProductByRevenue(int id, String name, int quantity, String catalogName, float revenue, float realRevenue) {
        this.id=id;
        this.name=name;
        this.quantitySales=quantity;
        this.catalogName=catalogName;
        this.revenue=revenue;
        this.realRevenue=realRevenue;
    }
}
