package com.olgun.bisiApp.service;

import com.olgun.bisiApp.model.Ilan;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SepetServisi {

    private static final String SEPET_ANAHTARI = "sepet";

    @SuppressWarnings("unchecked")
    private Set<Long> sepetiGetir(HttpSession session) {
        Object veri = session.getAttribute(SEPET_ANAHTARI);
        if (veri instanceof Set<?> mevcut) {
            return (Set<Long>) mevcut;
        }
        Set<Long> yeniSepet = new LinkedHashSet<>();
        session.setAttribute(SEPET_ANAHTARI, yeniSepet);
        return yeniSepet;
    }

    public void ekle(Long ilanId, HttpSession session) {
        sepetiGetir(session).add(ilanId);
    }

    public void kaldir(Long ilanId, HttpSession session) {
        sepetiGetir(session).remove(ilanId);
    }

    public void temizle(HttpSession session) {
        session.removeAttribute(SEPET_ANAHTARI);
    }

    public boolean sepetteMi(Long ilanId, HttpSession session) {
        return sepetiGetir(session).contains(ilanId);
    }

    public List<Long> sepetIdleri(HttpSession session) {
        return new ArrayList<>(sepetiGetir(session));
    }

    public BigDecimal toplamTutar(List<Ilan> ilanlar) {
        return ilanlar.stream()
                .map(Ilan::getFiyat)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
