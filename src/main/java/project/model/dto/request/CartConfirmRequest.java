package project.model.dto.request;

import lombok.Data;

@Data
public class CartConfirmRequest {
    private int discount;
    private float shipping;
    private float tax;
    private String note;
}
