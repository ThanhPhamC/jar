package project.model.dto.response;

import lombok.Data;

@Data
public class ProductByCatalogByCartStt extends RootResponse {
    private int quantity;
    private float exportPrice;
    private int disCount;
    private String catalogName;


}
