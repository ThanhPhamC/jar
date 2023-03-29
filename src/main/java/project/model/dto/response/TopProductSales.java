package project.model.dto.response;

import lombok.Data;

@Data
public class TopProductSales extends RootResponse {
    private int quantitySales;
    private  String catalogName;
    public TopProductSales(int id,String productName,int quantitySales,String catalogName){
        this.id=id;
        this.name=productName;
        this.quantitySales=quantitySales;
        this.catalogName=catalogName;
    }

}
