package com.olgun.bisiApp.service;

import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.KullaniciRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class KullaniciServisi {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public KullaniciServisi(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Kullanici kaydet(Kullanici kullanici) {
        if (kullanici == null
                || bosMu(kullanici.getAd())
                || bosMu(kullanici.getEposta())
                || bosMu(kullanici.getSifre())
                || bosMu(kullanici.getTelefon())) {
            throw new IllegalArgumentException("Eksik bilgi ile kayit olusturulamaz.");
        }

        if (kullaniciRepository.existsByEposta(kullanici.getEposta())) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kayitli.");
        }

        kullanici.setSifre(passwordEncoder.encode(kullanici.getSifre()));
        return kullaniciRepository.save(kullanici);
    }

    public Kullanici epostayaGoreBul(String eposta) {
        return kullaniciRepository.findByEposta(eposta)
                .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi."));
    }

    public List<Kullanici> tumKullanicilariGetir() {
        return kullaniciRepository.findAll();
    }

    private boolean bosMu(String deger) {
        return deger == null || deger.isBlank();
    }
}
