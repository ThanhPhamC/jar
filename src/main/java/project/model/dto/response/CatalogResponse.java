package project.model.dto.response;

import lombok.Data;

@Data
public class CatalogResponse {
    private int catalogId;
    private String catalogName;
    private boolean catalogStatus;
}
