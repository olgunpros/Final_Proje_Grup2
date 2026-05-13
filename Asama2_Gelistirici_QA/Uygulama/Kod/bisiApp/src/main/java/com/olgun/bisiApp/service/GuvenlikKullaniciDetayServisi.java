package com.olgun.bisiApp.service;

import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.KullaniciRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GuvenlikKullaniciDetayServisi implements UserDetailsService {

    private final KullaniciRepository kullaniciRepository;

    public GuvenlikKullaniciDetayServisi(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String eposta) throws UsernameNotFoundException {
        Kullanici kullanici = kullaniciRepository.findByEposta(eposta)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanici bulunamadi: " + eposta));

        return new User(
                kullanici.getEposta(),
                kullanici.getSifre(),
                AuthorityUtils.createAuthorityList("ROLE_KULLANICI")
        );
    }
}
