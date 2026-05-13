package com.olgun.bisiApp.controller;

import com.olgun.bisiApp.model.Ilan;
import com.olgun.bisiApp.service.IlanServisi;
import com.olgun.bisiApp.service.SepetServisi;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SepetController {

    private final SepetServisi sepetServisi;
    private final IlanServisi ilanServisi;

    public SepetController(SepetServisi sepetServisi, IlanServisi ilanServisi) {
        this.sepetServisi = sepetServisi;
        this.ilanServisi = ilanServisi;
    }

    @PostMapping("/sepet/ekle/{ilanId}")
    public String sepeteEkle(@PathVariable Long ilanId, HttpSession session) {
        sepetServisi.ekle(ilanId, session);
        return "redirect:/sepet";
    }

    @PostMapping("/sepet/kaldir/{ilanId}")
    public String sepettenKaldir(@PathVariable Long ilanId, HttpSession session) {
        sepetServisi.kaldir(ilanId, session);
        return "redirect:/sepet";
    }

    @GetMapping("/sepet")
    public String sepet(Model model, HttpSession session) {
        List<Ilan> ilanlar = ilanServisi.idlereGoreGetir(sepetServisi.sepetIdleri(session));
        BigDecimal toplam = sepetServisi.toplamTutar(ilanlar);
        BigDecimal hizmetBedeli = toplam.multiply(new BigDecimal("0.03"));
        BigDecimal genelToplam = toplam.add(hizmetBedeli);

        model.addAttribute("sepetIlanlari", ilanlar);
        model.addAttribute("toplam", toplam);
        model.addAttribute("hizmetBedeli", hizmetBedeli);
        model.addAttribute("genelToplam", genelToplam);
        return "sepet";
    }

    @PostMapping("/sepet/satin-al")
    public String satinAl(HttpSession session) {
        sepetServisi.temizle(session);
        return "redirect:/sepet?tamamlandi";
    }
}
