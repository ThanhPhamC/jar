package project.model.dto.response;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogResponse extends RootResponse{
    private LocalDateTime creatDate;
    private String blogImg;
    private String content;
    private String userName;
}
