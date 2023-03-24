package project.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AddressRevenue extends RootResponse{
    private int countOder;
    private float revenue;
    private LocalDate saleDate;
    private float ship;
    private float tax;
    private float totalRevenue;
}
