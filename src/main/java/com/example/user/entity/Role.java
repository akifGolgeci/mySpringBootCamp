package com.example.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//child
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY) // bir nesne olduğu için defaulteı EAGER 'dır.
    // EAGER olunca çağrıldığında tüm bilgiler döner.
    // LAZY 'DE ise sadece işlemler yapılacağı zaman veriler çağrılır.
    // LAZY diyerek performansı arttırmış oluyoruz
    //@JoinColumn(name = "user_id")
    //<değişken_adı>_<hedef_tablonun_id_kolon_adı> şeklinde tabloda cloumn adı oluşur
    // user_id şeklindedir
    @JsonBackReference
    private User user;

}
