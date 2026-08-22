package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   /* // 1.İsme göre artan sırala
    List<User> findAllByOrderByFirstNameAsc();

    // 2.İsme göre azalan sırala
    List<User> findAllByOrderByFirstNameDesc();

    // Tarihe göre artan sırala
    List<User> findAllByOrderByCreatedDateTimeAsc();

    // Tarihe göre AZALAN sırala
    List<User> findAllByOrderByCreatedDateTimeDesc();

    //İsmi belirli bir stringi içeren büyük harf küçük harf değişmez.
    List<User> findAllByFirstNameContainingIgnoreCase(String firstName);

    //İsmi belirli bir harfi içeren büyük harf küçük harf değişmez.
    List<User> findAllByFirstNameContaining(String a);

    //SOYADI girilen harfle başlayan
    List<User> findAllByLastNameStartingWith(String lastName);

    //Emaili @gmail.com ile biten
    List<User> findAllByEmailEndingWith(String email);

    //isim ve soyisimle bir kişiyi bulma
    Optional<User> findAllByFirstNameAndLastName(String firstName, String lastName);

    //namei null olmayan
    List<User> findByFirstNameNotNull();

    //Belirli tarihler arasında oluşturulan
    List<User> findByCreatedDateTimeBetween(LocalDateTime start, LocalDateTime end);

    //belirli tarihten sonra oluşturulan
    List<User> findByCreatedDateTimeAfter(LocalDateTime date);

    //belirli tarihten önce oluşturulan
    List<User> findByCreatedDateTimeBefore(LocalDateTime date);

    //isme göre artan sıralma ama ilk 5 sonucu getir
    List<User> findTop5ByOrderByFirstNameAsc();

*/

}
