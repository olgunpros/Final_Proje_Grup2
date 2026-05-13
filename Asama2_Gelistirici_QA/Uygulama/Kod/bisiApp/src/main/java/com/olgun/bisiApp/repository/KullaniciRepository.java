package com.olgun.bisiApp.repository;

import com.olgun.bisiApp.model.Kullanici;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {

    Optional<Kullanici> findByEposta(String eposta);

    boolean existsByEposta(String eposta);
}
