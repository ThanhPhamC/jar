package project.model.dto.response;
import lombok.Data;
import project.model.entity.CommentBlog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogResponse extends RootResponse{
    private LocalDateTime creatDate;
    private String blogImg;
    private String content;
    private String userName;
    private int countComment;
    private List<CommentBlog> listCommentBlog = new ArrayList<>();
}
