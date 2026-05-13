package com.olgun.bisiApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.olgun.bisiApp.model.Ilan;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

class SepetServisiBirimTest {

    private SepetServisi sepetServisi;
    private MockHttpSession session;

    @BeforeEach
    void hazirlik() {
        sepetServisi = new SepetServisi();
        session = new MockHttpSession();
    }

    @Test
    void sepeteEklenenIlanSepetteGorunurTesti() {
        sepetServisi.ekle(10L, session);

        assertTrue(sepetServisi.sepetteMi(10L, session));
        assertEquals(List.of(10L), sepetServisi.sepetIdleri(session));
    }

    @Test
    void sepettenKaldirilanIlanArtikGorunmezTesti() {
        sepetServisi.ekle(10L, session);
        sepetServisi.ekle(20L, session);

        sepetServisi.kaldir(10L, session);

        assertFalse(sepetServisi.sepetteMi(10L, session));
        assertEquals(List.of(20L), sepetServisi.sepetIdleri(session));
    }

    @Test
    void toplamTutarIlanFiyatlariniToplarTesti() {
        Ilan ilkIlan = new Ilan();
        ilkIlan.setFiyat(new BigDecimal("15000"));

        Ilan ikinciIlan = new Ilan();
        ikinciIlan.setFiyat(new BigDecimal("25500"));

        BigDecimal toplam = sepetServisi.toplamTutar(List.of(ilkIlan, ikinciIlan));

        assertEquals(new BigDecimal("40500"), toplam);
    }
}
