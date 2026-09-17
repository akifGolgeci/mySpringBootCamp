package com.example.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "car")
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 35)
    @Column(name = "brand", length = 35)
    private String brand;

    @Column(name = "year")
    private Integer year;

    @Size(max = 35)
    @Column(name = "color", length = 35)
    private String color;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @ColumnDefault("false")
    @Column(name = "otomatic")
    private Boolean otomatic;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_date")
    private Instant createdDate;


}