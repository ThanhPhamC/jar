package project.model.dto.request;

import lombok.Data;

@Data
public class CatalogRequest {
    private int catalogId;
    private String catalogName;
    private boolean catalogStatus;
}
