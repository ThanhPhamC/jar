package project.model.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BlogRequest {
    private String name;
    private String blogImg;
    private String content;
    private int userId;
    private int status;
    private int catalogOfBlogId;
}
