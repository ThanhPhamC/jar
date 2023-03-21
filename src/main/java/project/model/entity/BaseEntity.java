package project.model.entity;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(columnDefinition = "nvarchar(100)", unique = true)
    protected String name;
    protected int status;
}
