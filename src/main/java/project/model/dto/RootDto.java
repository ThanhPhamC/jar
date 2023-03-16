package project.model.dto;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class RootDto {
    private int Id;
    private String Name;
    private int Status;
}
