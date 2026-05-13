package com.olgun.bisiApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.olgun.bisiApp.model.Ilan;
import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.IlanRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class IlanServisiBirimTest {

    @Mock
    private IlanRepository ilanRepository;

    @InjectMocks
    private IlanServisi ilanServisi;

    private Kullanici satici;
    private Ilan ilan;

    @BeforeEach
    void hazirlik() {
        satici = new Kullanici();
        satici.setId(1L);
        satici.setAd("Ayse Demir");

        ilan = new Ilan();
        ilan.setBaslik("Carraro Yarış Bisikleti");
        ilan.setKategori("Yarış Bisikleti");
        ilan.setFiyat(new BigDecimal("24500"));
    }

    @Test
    void basariliIlanEklemeTesti() {
        when(ilanRepository.save(any(Ilan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ilan kaydedilenIlan = ilanServisi.kaydet(ilan, satici);

        assertEquals(satici, kaydedilenIlan.getSatici());
        assertEquals("Carraro Yarış Bisikleti", kaydedilenIlan.getBaslik());
        verify(ilanRepository).save(ilan);
    }

    @Test
    void fiyatVeKategoriyeGoreFiltrelemeTesti() {
        List<Ilan> beklenenIlanlar = List.of(ilan);
        when(ilanRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(beklenenIlanlar);

        List<Ilan> sonuc = ilanServisi.ilanlariFiltrele(
                null,
                new BigDecimal("20000"),
                new BigDecimal("30000"),
                "Yarış Bisikleti",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArgumentCaptor<Specification<Ilan>> ozellikYakalayici = ArgumentCaptor.forClass(Specification.class);
        ArgumentCaptor<Sort> siralamaYakalayici = ArgumentCaptor.forClass(Sort.class);
        verify(ilanRepository).findAll(ozellikYakalayici.capture(), siralamaYakalayici.capture());

        assertEquals(1, sonuc.size());
        assertEquals("Carraro Yarış Bisikleti", sonuc.getFirst().getBaslik());
        assertNotNull(ozellikYakalayici.getValue());
        assertFalse(siralamaYakalayici.getValue().isUnsorted());
        assertEquals(Sort.by(Sort.Direction.DESC, "id"), siralamaYakalayici.getValue());
    }

    @Test
    void ilanSahibiGuncellemeYapabilirTesti() {
        Ilan mevcutIlan = new Ilan();
        mevcutIlan.setId(10L);
        mevcutIlan.setBaslik("Eski Baslik");
        mevcutIlan.setFiyat(new BigDecimal("12000"));
        mevcutIlan.setKategori("Sehir Bisikleti");
        mevcutIlan.setSatici(satici);

        Ilan guncelIlan = new Ilan();
        guncelIlan.setBaslik("Guncel Baslik");
        guncelIlan.setFiyat(new BigDecimal("18000"));
        guncelIlan.setKategori("Dag Bisikleti");
        guncelIlan.setMarka("Carraro");
        guncelIlan.setRenk("Siyah");
        guncelIlan.setModelYili(2025);
        guncelIlan.setVitesSayisi(12);
        guncelIlan.setDurum("Cok Iyi");
        guncelIlan.setAciklama("Bakimli ilan");
        guncelIlan.setResimUrl("/gorseller/ornek.jpg");

        when(ilanRepository.findById(10L)).thenReturn(Optional.of(mevcutIlan));
        when(ilanRepository.save(any(Ilan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ilan sonuc = ilanServisi.guncelle(10L, guncelIlan, satici);

        assertEquals("Guncel Baslik", sonuc.getBaslik());
        assertEquals(new BigDecimal("18000"), sonuc.getFiyat());
        assertEquals("Dag Bisikleti", sonuc.getKategori());
        assertEquals("Carraro", sonuc.getMarka());
        verify(ilanRepository).save(mevcutIlan);
    }

    @Test
    void yetkisizKullaniciIlanSilemezTesti() {
        Kullanici baskaKullanici = new Kullanici();
        baskaKullanici.setId(99L);

        ilan.setId(15L);
        ilan.setSatici(satici);

        when(ilanRepository.findById(15L)).thenReturn(Optional.of(ilan));

        IllegalArgumentException hata = assertThrows(
                IllegalArgumentException.class,
                () -> ilanServisi.sil(15L, baskaKullanici)
        );

        assertEquals("Bu ilana islem yapma yetkiniz yok.", hata.getMessage());
        verify(ilanRepository, never()).delete(any(Ilan.class));
    }

    @Test
    void idlereGoreGetirIstenenSirayiKorumalidirTesti() {
        Ilan ilkIlan = new Ilan();
        ilkIlan.setId(1L);
        ilkIlan.setBaslik("Birinci");

        Ilan ikinciIlan = new Ilan();
        ikinciIlan.setId(2L);
        ikinciIlan.setBaslik("Ikinci");

        when(ilanRepository.findAllById(eq(List.of(2L, 1L)))).thenReturn(List.of(ilkIlan, ikinciIlan));

        List<Ilan> sonuc = ilanServisi.idlereGoreGetir(List.of(2L, 1L));

        assertEquals(2, sonuc.size());
        assertEquals(2L, sonuc.get(0).getId());
        assertEquals(1L, sonuc.get(1).getId());
        verify(ilanRepository, times(1)).findAllById(List.of(2L, 1L));
    }
}
