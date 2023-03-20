package project.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
public class CatalogOfBlogRequest {
    private String name;
    private int status;
}
