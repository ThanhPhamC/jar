package project.model.dto.response;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse extends RootResponse{
    private List<CartDetailResponse> detailResponses= new ArrayList<>();
}
