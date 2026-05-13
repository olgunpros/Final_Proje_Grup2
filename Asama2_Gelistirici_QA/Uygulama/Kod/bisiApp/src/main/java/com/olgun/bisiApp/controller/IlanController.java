package com.olgun.bisiApp.controller;

import com.olgun.bisiApp.model.Ilan;
import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.service.DosyaServisi;
import com.olgun.bisiApp.service.IlanServisi;
import com.olgun.bisiApp.service.KullaniciServisi;
import com.olgun.bisiApp.service.SepetServisi;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class IlanController {

    private final IlanServisi ilanServisi;
    private final KullaniciServisi kullaniciServisi;
    private final DosyaServisi dosyaServisi;
    private final SepetServisi sepetServisi;

    public IlanController(
            IlanServisi ilanServisi,
            KullaniciServisi kullaniciServisi,
            DosyaServisi dosyaServisi,
            SepetServisi sepetServisi
    ) {
        this.ilanServisi = ilanServisi;
        this.kullaniciServisi = kullaniciServisi;
        this.dosyaServisi = dosyaServisi;
        this.sepetServisi = sepetServisi;
    }

    @GetMapping("/ilanlar/{id}")
    public String ilanDetay(@PathVariable Long id, Model model, Principal principal, HttpSession session) {
        Ilan ilan = ilanServisi.idyeGoreGetir(id);
        boolean sahibiMi = principal != null && ilan.getSatici().getEposta().equals(principal.getName());
        model.addAttribute("ilan", ilan);
        model.addAttribute("sahibiMi", sahibiMi);
        model.addAttribute("sepetteMi", sepetServisi.sepetteMi(id, session));
        return "ilan-detay";
    }

    @GetMapping("/ilan/ekle")
    public String ilanEkleSayfasi(Model model) {
        model.addAttribute("ilan", new Ilan());
        ortakFormAlanlari(model);
        model.addAttribute("formBaslik", "Yeni Ilan Ekle");
        model.addAttribute("formAksiyon", "/ilan/ekle");
        return "ilan-form";
    }

    @PostMapping("/ilan/ekle")
    public String ilanEkle(
            @ModelAttribute Ilan ilan,
            @RequestParam("resimDosyasi") MultipartFile resimDosyasi,
            Authentication authentication
    ) {
        Kullanici kullanici = kullaniciServisi.epostayaGoreBul(authentication.getName());
        String resimYolu = dosyaServisi.kaydet(resimDosyasi);
        if (resimYolu != null) {
            ilan.setResimUrl(resimYolu);
        }
        ilanServisi.kaydet(ilan, kullanici);
        return "redirect:/";
    }

    @GetMapping("/ilan/duzenle/{id}")
    public String ilanDuzenleSayfasi(@PathVariable Long id, Principal principal, Model model) {
        Ilan ilan = ilanServisi.idyeGoreGetir(id);
        Kullanici kullanici = kullaniciServisi.epostayaGoreBul(principal.getName());
        if (!ilan.getSatici().getId().equals(kullanici.getId())) {
            throw new IllegalArgumentException("Bu ilana islem yapma yetkiniz yok.");
        }
        model.addAttribute("ilan", ilan);
        ortakFormAlanlari(model);
        model.addAttribute("formBaslik", "Ilani Guncelle");
        model.addAttribute("formAksiyon", "/ilan/duzenle/" + id);
        return "ilan-form";
    }

    @PostMapping("/ilan/duzenle/{id}")
    public String ilanDuzenle(
            @PathVariable Long id,
            @ModelAttribute Ilan ilan,
            @RequestParam("resimDosyasi") MultipartFile resimDosyasi,
            Principal principal
    ) {
        Kullanici kullanici = kullaniciServisi.epostayaGoreBul(principal.getName());
        Ilan mevcut = ilanServisi.idyeGoreGetir(id);
        String resimYolu = dosyaServisi.kaydet(resimDosyasi);
        if (resimYolu != null) {
            ilan.setResimUrl(resimYolu);
        } else {
            ilan.setResimUrl(mevcut.getResimUrl());
        }
        ilanServisi.guncelle(id, ilan, kullanici);
        return "redirect:/ilanlar/" + id;
    }

    @PostMapping("/ilan/sil/{id}")
    public String ilanSil(@PathVariable Long id, Principal principal) {
        Kullanici kullanici = kullaniciServisi.epostayaGoreBul(principal.getName());
        ilanServisi.sil(id, kullanici);
        return "redirect:/";
    }

    @GetMapping("/ilanlarim")
    public String ilanlarim(Principal principal, Model model) {
        Kullanici kullanici = kullaniciServisi.epostayaGoreBul(principal.getName());
        model.addAttribute("ilanlar", ilanServisi.kullanicininIlanlari(kullanici));
        model.addAttribute("kullanici", kullanici);
        return "ilanlarim";
    }

    private void ortakFormAlanlari(Model model) {
        model.addAttribute("kategoriler", ilanServisi.kategorileriGetir());
        model.addAttribute("markalar", ilanServisi.markalariGetir());
        model.addAttribute("renkler", ilanServisi.renkleriGetir());
        model.addAttribute("durumlar", ilanServisi.durumlariGetir());
    }
}
