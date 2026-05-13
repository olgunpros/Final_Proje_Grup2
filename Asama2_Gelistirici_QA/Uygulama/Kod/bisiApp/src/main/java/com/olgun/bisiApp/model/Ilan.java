package com.olgun.bisiApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Ilan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baslik;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal fiyat;

    @Column(nullable = false)
    private String kategori;

    private String marka;

    private String renk;

    private Integer modelYili;

    private Integer vitesSayisi;

    private String durum;

    @Column(nullable = false, length = 2000)
    private String aciklama;

    private String resimUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "satici_id", nullable = false)
    private Kullanici satici;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public BigDecimal getFiyat() {
        return fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public Integer getModelYili() {
        return modelYili;
    }

    public void setModelYili(Integer modelYili) {
        this.modelYili = modelYili;
    }

    public Integer getVitesSayisi() {
        return vitesSayisi;
    }

    public void setVitesSayisi(Integer vitesSayisi) {
        this.vitesSayisi = vitesSayisi;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public Kullanici getSatici() {
        return satici;
    }

    public void setSatici(Kullanici satici) {
        this.satici = satici;
    }
}
