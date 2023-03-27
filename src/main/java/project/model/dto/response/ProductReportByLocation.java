package project.model.dto.response;


import lombok.Data;

@Data
public class ProductReportByLocation extends RootResponse{
    private int quantitySales;
    private float revenue;
    private float discount;
    private float realRevenue;
    private  String locationName;
}
