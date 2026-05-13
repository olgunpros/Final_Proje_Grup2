package com.olgun.bisiApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.KullaniciRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class KullaniciServisiBirimTest {

    @Mock
    private KullaniciRepository kullaniciRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private KullaniciServisi kullaniciServisi;

    private Kullanici kullanici;

    @BeforeEach
    void hazirlik() {
        kullanici = new Kullanici();
        kullanici.setAd("Ali Vural");
        kullanici.setEposta("ali@example.com");
        kullanici.setSifre("123456");
        kullanici.setTelefon("05550000000");
    }

    @Test
    void basariliKayitOlmaTesti() {
        when(kullaniciRepository.existsByEposta("ali@example.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("kodlanmisSifre");
        when(kullaniciRepository.save(any(Kullanici.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Kullanici kaydedilenKullanici = kullaniciServisi.kaydet(kullanici);

        assertEquals("kodlanmisSifre", kaydedilenKullanici.getSifre());
        assertEquals("ali@example.com", kaydedilenKullanici.getEposta());
        verify(kullaniciRepository).save(kullanici);
    }

    @Test
    void eksikBilgiyleKayitOlamamaTesti() {
        kullanici.setTelefon(" ");

        IllegalArgumentException hata = assertThrows(
                IllegalArgumentException.class,
                () -> kullaniciServisi.kaydet(kullanici)
        );

        assertEquals("Eksik bilgi ile kayit olusturulamaz.", hata.getMessage());
        verify(kullaniciRepository, never()).save(any(Kullanici.class));
    }

    @Test
    void ayniEpostaIleIkinciKayitEngellenirTesti() {
        when(kullaniciRepository.existsByEposta("ali@example.com")).thenReturn(true);

        IllegalArgumentException hata = assertThrows(
                IllegalArgumentException.class,
                () -> kullaniciServisi.kaydet(kullanici)
        );

        assertEquals("Bu e-posta adresi zaten kayitli.", hata.getMessage());
        verify(passwordEncoder, never()).encode(any());
        verify(kullaniciRepository, never()).save(any(Kullanici.class));
    }

    @Test
    void epostayaGoreBulunanKullaniciDondurulurTesti() {
        when(kullaniciRepository.findByEposta("ali@example.com")).thenReturn(Optional.of(kullanici));

        Kullanici bulunan = kullaniciServisi.epostayaGoreBul("ali@example.com");

        assertEquals("Ali Vural", bulunan.getAd());
        assertEquals("05550000000", bulunan.getTelefon());
    }

    @Test
    void bulunamayanEpostaIcinHataFirlatilirTesti() {
        when(kullaniciRepository.findByEposta("yok@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException hata = assertThrows(
                IllegalArgumentException.class,
                () -> kullaniciServisi.epostayaGoreBul("yok@example.com")
        );

        assertEquals("Kullanici bulunamadi.", hata.getMessage());
    }
}
