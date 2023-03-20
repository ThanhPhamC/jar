package project.model.dto.response;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BlogResponse extends RootResponse{
    private LocalDate creatDate;
    private String blogImg;
    private String content;
    private String userName;
}
