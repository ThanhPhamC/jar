package project.model.dto.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse extends RootResponse{
    private String firstName;
    private String lastName;
    private  String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private  String state;
    private float discount;
    private float shipping;
    private float tax;
    private List<CartDetailResponse> detailResponses= new ArrayList<>();
}
