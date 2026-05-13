package com.olgun.bisiApp.controller;

import com.olgun.bisiApp.service.DosyaServisi;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GorselController {

    private final DosyaServisi dosyaServisi;

    public GorselController(DosyaServisi dosyaServisi) {
        this.dosyaServisi = dosyaServisi;
    }

    @GetMapping("/gorseller/{dosyaAdi}")
    public ResponseEntity<Resource> gorselGetir(@PathVariable String dosyaAdi) {
        Resource kaynak = dosyaServisi.yukle(dosyaAdi);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + dosyaAdi + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(kaynak);
    }
}
