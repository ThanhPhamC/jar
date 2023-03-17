package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "blog")
public class Blog extends BaseEntity{
    private LocalDate creatDate;
    @Column(columnDefinition = "text")
    private String blogImg;
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
}
