package com.olgun.bisiApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.olgun.bisiApp.model.Ilan;
import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.IlanRepository;
import com.olgun.bisiApp.repository.KullaniciRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IlanServisiEntegrasyonTest {

    @Autowired
    private IlanServisi ilanServisi;

    @Autowired
    private IlanRepository ilanRepository;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Test
    void ilanEklendigindeVeritabanindanDogruSekildeListelenir() {
        Kullanici kullanici = new Kullanici();
        kullanici.setAd("Test Kullanıcı");
        kullanici.setEposta("testkullanici@example.com");
        kullanici.setSifre("sifre");
        kullanici.setTelefon("05551112233");
        kullaniciRepository.save(kullanici);

        Ilan ilan = new Ilan();
        ilan.setBaslik("Bianchi Sprint");
        ilan.setFiyat(new BigDecimal("52000"));
        ilan.setKategori("Yarış Bisikleti");
        ilan.setMarka("Bianchi");
        ilan.setRenk("Siyah");
        ilan.setModelYili(2024);
        ilan.setVitesSayisi(24);
        ilan.setDurum("Çok İyi");
        ilan.setAciklama("Bakımlı ve temiz kullanılmış performans bisikleti.");

        ilanServisi.kaydet(ilan, kullanici);

        List<Ilan> ilanlar = ilanServisi.ilanlariFiltrele(
                null,
                new BigDecimal("50000"),
                new BigDecimal("53000"),
                "Yarış Bisikleti",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertFalse(ilanlar.isEmpty());
        assertEquals(1, ilanRepository.count());
        assertEquals("Bianchi Sprint", ilanlar.getFirst().getBaslik());
    }
}
