package com.example.user.entity;

import jakarta.persistence.*;
import lombok.*;

//child
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String port;
    
    @OneToOne
    @JoinColumn
    private User user;
}
