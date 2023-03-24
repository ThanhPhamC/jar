package project.model.dto.response;

import lombok.Data;
import project.model.entity.BaseEntity;
@Data
public class ProductReportByCatalog extends RootResponse {
    private int quantitySales;
    private float revenue;
    private float discount;
    private float realRevenue;
    private  String catalogName;

}
