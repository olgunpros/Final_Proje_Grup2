package com.olgun.bisiApp.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DosyaServisi {

    private final Path kokDizin = Paths.get("yuklenen-resimler").toAbsolutePath().normalize();

    public String kaydet(MultipartFile dosya) {
        if (dosya == null || dosya.isEmpty()) {
            return null;
        }

        try {
            Files.createDirectories(kokDizin);
            String orijinalAd = dosya.getOriginalFilename() == null ? "resim" : dosya.getOriginalFilename();
            String temizAd = orijinalAd.replaceAll("[^a-zA-Z0-9._-]", "_");
            String yeniAd = UUID.randomUUID() + "_" + temizAd;
            Path hedef = kokDizin.resolve(yeniAd);

            try (InputStream giris = dosya.getInputStream()) {
                Files.copy(giris, hedef, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/gorseller/" + yeniAd;
        } catch (IOException ex) {
            throw new IllegalStateException("Resim dosyasi kaydedilemedi.", ex);
        }
    }

    public Resource yukle(String dosyaAdi) {
        try {
            Path dosya = kokDizin.resolve(dosyaAdi).normalize();
            Resource kaynak = new UrlResource(dosya.toUri());
            if (kaynak.exists() && kaynak.isReadable()) {
                return kaynak;
            }
            throw new IllegalArgumentException("Gorsel bulunamadi.");
        } catch (IOException ex) {
            throw new IllegalStateException("Gorsel okunamadi.", ex);
        }
    }
}
