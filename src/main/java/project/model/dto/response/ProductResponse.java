package project.model.dto.response;

import lombok.Data;

@Data
public class ProductResponse extends RootResponse{
    private int discount;
    private String productImg;
    private float exportPrice;
}
