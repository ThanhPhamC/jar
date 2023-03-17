package project.model.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogResponse extends RootResponse{
    private LocalDate creatDate;
    private String blogImg;
    private String content;
    private String userName;
}
