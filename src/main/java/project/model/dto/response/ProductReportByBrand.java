package project.model.dto.response;

import lombok.Data;

@Data
public class ProductReportByBrand extends RootResponse{
    private int quantitySales;
    private float revenue;
    private float discount;
    private float realRevenue;
    private  String brandName;
}
