package com.example.user2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car2")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @Column(name = "brand", length = 30)
    private String brand;

    @Size(max = 20)
    @Column(name = "model", length = 20)
    private String model;

    @Column(name = "price")
    private Integer price;

    @Column(name = "year")
    private Integer year;
}
