package com.olgun.bisiApp.service;

import com.olgun.bisiApp.model.Ilan;
import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.IlanRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class IlanServisi {

    private final IlanRepository ilanRepository;

    public IlanServisi(IlanRepository ilanRepository) {
        this.ilanRepository = ilanRepository;
    }

    public Ilan kaydet(Ilan ilan, Kullanici satici) {
        ilan.setSatici(satici);
        return ilanRepository.save(ilan);
    }

    public Ilan guncelle(Long id, Ilan guncelIlan, Kullanici kullanici) {
        Ilan mevcutIlan = idyeGoreGetir(id);
        sahiplikKontrolu(mevcutIlan, kullanici);

        mevcutIlan.setBaslik(guncelIlan.getBaslik());
        mevcutIlan.setFiyat(guncelIlan.getFiyat());
        mevcutIlan.setKategori(guncelIlan.getKategori());
        mevcutIlan.setMarka(guncelIlan.getMarka());
        mevcutIlan.setRenk(guncelIlan.getRenk());
        mevcutIlan.setModelYili(guncelIlan.getModelYili());
        mevcutIlan.setVitesSayisi(guncelIlan.getVitesSayisi());
        mevcutIlan.setDurum(guncelIlan.getDurum());
        mevcutIlan.setAciklama(guncelIlan.getAciklama());
        mevcutIlan.setResimUrl(guncelIlan.getResimUrl());
        return ilanRepository.save(mevcutIlan);
    }

    public void sil(Long id, Kullanici kullanici) {
        Ilan ilan = idyeGoreGetir(id);
        sahiplikKontrolu(ilan, kullanici);
        ilanRepository.delete(ilan);
    }

    public List<Ilan> ilanlariFiltrele(
            String aramaMetni,
            BigDecimal minFiyat,
            BigDecimal maxFiyat,
            String kategori,
            String marka,
            String renk,
            Integer minYil,
            Integer maxYil,
            Integer minVites,
            Integer maxVites,
            String durum
    ) {
        Specification<Ilan> ozellik = (root, query, criteriaBuilder) -> {
            List<Predicate> kosullar = new ArrayList<>();

            if (aramaMetni != null && !aramaMetni.isBlank()) {
                kosullar.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("baslik")),
                        "%" + aramaMetni.toLowerCase() + "%"
                ));
            }
            if (minFiyat != null) {
                kosullar.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fiyat"), minFiyat));
            }
            if (maxFiyat != null) {
                kosullar.add(criteriaBuilder.lessThanOrEqualTo(root.get("fiyat"), maxFiyat));
            }
            if (kategori != null && !kategori.isBlank()) {
                kosullar.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("kategori")), kategori.toLowerCase()));
            }
            if (marka != null && !marka.isBlank()) {
                kosullar.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("marka")), marka.toLowerCase()));
            }
            if (renk != null && !renk.isBlank()) {
                kosullar.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("renk")), renk.toLowerCase()));
            }
            if (minYil != null) {
                kosullar.add(criteriaBuilder.greaterThanOrEqualTo(root.get("modelYili"), minYil));
            }
            if (maxYil != null) {
                kosullar.add(criteriaBuilder.lessThanOrEqualTo(root.get("modelYili"), maxYil));
            }
            if (minVites != null) {
                kosullar.add(criteriaBuilder.greaterThanOrEqualTo(root.get("vitesSayisi"), minVites));
            }
            if (maxVites != null) {
                kosullar.add(criteriaBuilder.lessThanOrEqualTo(root.get("vitesSayisi"), maxVites));
            }
            if (durum != null && !durum.isBlank()) {
                kosullar.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("durum")), durum.toLowerCase()));
            }

            return criteriaBuilder.and(kosullar.toArray(new Predicate[0]));
        };

        return ilanRepository.findAll(ozellik, Sort.by(Sort.Direction.DESC, "id"));
    }

    public Ilan idyeGoreGetir(Long id) {
        return ilanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ilan bulunamadi."));
    }

    public List<Ilan> idlereGoreGetir(List<Long> ilanIdleri) {
        if (ilanIdleri.isEmpty()) {
            return List.of();
        }
        List<Ilan> ilanlar = ilanRepository.findAllById(ilanIdleri);
        Set<Long> sira = new LinkedHashSet<>(ilanIdleri);
        return ilanlar.stream()
                .sorted((ilk, ikinci) -> Integer.compare(
                        new ArrayList<>(sira).indexOf(ilk.getId()),
                        new ArrayList<>(sira).indexOf(ikinci.getId())
                ))
                .toList();
    }

    public List<Ilan> kullanicininIlanlari(Kullanici kullanici) {
        return ilanRepository.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("satici"), kullanici),
                Sort.by(Sort.Direction.DESC, "id"));
    }

    public Set<String> kategorileriGetir() {
        return Set.of("Dağ Bisikleti", "Yarış Bisikleti", "Şehir Bisikleti", "Çocuk Bisikleti", "Elektrikli");
    }

    public Set<String> markalariGetir() {
        return Set.of("Bianchi", "Carraro", "Salcano", "Trek", "Scott", "Kron", "Mosso");
    }

    public Set<String> renkleriGetir() {
        return Set.of("Siyah", "Beyaz", "Kırmızı", "Mavi", "Yeşil", "Gri", "Turuncu");
    }

    public Set<String> durumlariGetir() {
        return Set.of("Sıfır Ayarında", "Çok İyi", "İyi", "Kullanılmış", "Bakım Gerekli");
    }

    private void sahiplikKontrolu(Ilan ilan, Kullanici kullanici) {
        if (!ilan.getSatici().getId().equals(kullanici.getId())) {
            throw new IllegalArgumentException("Bu ilana islem yapma yetkiniz yok.");
        }
    }
}
