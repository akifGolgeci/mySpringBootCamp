package com.example.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//parent
public class Capital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @OneToOne(mappedBy = "capital",cascade = CascadeType.ALL,orphanRemoval = true)
    private Country country;

}
