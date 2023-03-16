package project.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "slider")
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sliderId;
    private String sliderName;
    private String sliderDescription;
    private String sliderBackground;
    private boolean sliderStatus;
}
