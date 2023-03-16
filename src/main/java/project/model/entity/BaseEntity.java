package project.model.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(columnDefinition = "nvarchar(100)")
    protected String name;
    protected int status;
}
