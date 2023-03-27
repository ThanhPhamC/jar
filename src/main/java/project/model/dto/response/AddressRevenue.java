package project.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AddressRevenue {
    private int id;
    private LocalDate saleDate;
    private float revenue;
    private int countOder;
    private float ship;
    private float tax;
    private float discount;
    private float totalRevenue;
}
