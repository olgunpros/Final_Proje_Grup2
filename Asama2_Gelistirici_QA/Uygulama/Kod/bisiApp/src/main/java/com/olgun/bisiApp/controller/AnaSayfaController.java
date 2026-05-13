package com.olgun.bisiApp.controller;

import com.olgun.bisiApp.service.IlanServisi;
import com.olgun.bisiApp.service.KullaniciServisi;
import com.olgun.bisiApp.service.SepetServisi;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnaSayfaController {

    private final IlanServisi ilanServisi;
    private final KullaniciServisi kullaniciServisi;
    private final SepetServisi sepetServisi;

    public AnaSayfaController(IlanServisi ilanServisi, KullaniciServisi kullaniciServisi, SepetServisi sepetServisi) {
        this.ilanServisi = ilanServisi;
        this.kullaniciServisi = kullaniciServisi;
        this.sepetServisi = sepetServisi;
    }

    @GetMapping("/")
    public String anaSayfa(
            @RequestParam(required = false) String aramaMetni,
            @RequestParam(required = false) BigDecimal minFiyat,
            @RequestParam(required = false) BigDecimal maxFiyat,
            @RequestParam(required = false) String kategori,
            @RequestParam(required = false) String marka,
            @RequestParam(required = false) String renk,
            @RequestParam(required = false) Integer minYil,
            @RequestParam(required = false) Integer maxYil,
            @RequestParam(required = false) Integer minVites,
            @RequestParam(required = false) Integer maxVites,
            @RequestParam(required = false) String durum,
            Principal principal,
            HttpSession session,
            Model model
    ) {
        model.addAttribute("ilanlar", ilanServisi.ilanlariFiltrele(
                aramaMetni, minFiyat, maxFiyat, kategori, marka, renk, minYil, maxYil, minVites, maxVites, durum
        ));
        if (principal != null) {
            model.addAttribute("kullanici", kullaniciServisi.epostayaGoreBul(principal.getName()));
        }
        model.addAttribute("aramaMetni", aramaMetni);
        model.addAttribute("minFiyat", minFiyat);
        model.addAttribute("maxFiyat", maxFiyat);
        model.addAttribute("kategori", kategori);
        model.addAttribute("marka", marka);
        model.addAttribute("renk", renk);
        model.addAttribute("minYil", minYil);
        model.addAttribute("maxYil", maxYil);
        model.addAttribute("minVites", minVites);
        model.addAttribute("maxVites", maxVites);
        model.addAttribute("durum", durum);
        model.addAttribute("girisYapmisMi", principal != null);
        model.addAttribute("kategoriler", ilanServisi.kategorileriGetir());
        model.addAttribute("markalar", ilanServisi.markalariGetir());
        model.addAttribute("renkler", ilanServisi.renkleriGetir());
        model.addAttribute("durumlar", ilanServisi.durumlariGetir());
        model.addAttribute("sepettekiIlanIdleri", sepetServisi.sepetIdleri(session));
        model.addAttribute("sepetAdedi", sepetServisi.sepetIdleri(session).size());
        return "anasayfa";
    }
}
