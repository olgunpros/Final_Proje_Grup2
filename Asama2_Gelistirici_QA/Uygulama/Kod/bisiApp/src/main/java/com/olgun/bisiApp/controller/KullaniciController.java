package com.olgun.bisiApp.controller;

import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.service.KullaniciServisi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KullaniciController {

    private final KullaniciServisi kullaniciServisi;

    public KullaniciController(KullaniciServisi kullaniciServisi) {
        this.kullaniciServisi = kullaniciServisi;
    }

    @GetMapping("/kayit")
    public String kayitSayfasi(Model model) {
        model.addAttribute("kullanici", new Kullanici());
        return "kayit";
    }

    @PostMapping("/kayit")
    public String kayitOlustur(@ModelAttribute Kullanici kullanici, Model model) {
        try {
            kullaniciServisi.kaydet(kullanici);
            return "redirect:/giris";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("hata", ex.getMessage());
            model.addAttribute("kullanici", kullanici);
            return "kayit";
        }
    }

    @GetMapping("/giris")
    public String girisSayfasi() {
        return "giris";
    }
}
