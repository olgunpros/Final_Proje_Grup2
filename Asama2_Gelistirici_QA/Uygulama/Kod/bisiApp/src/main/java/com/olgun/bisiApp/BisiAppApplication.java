package com.olgun.bisiApp;

import com.olgun.bisiApp.model.Ilan;
import com.olgun.bisiApp.model.Kullanici;
import com.olgun.bisiApp.repository.IlanRepository;
import com.olgun.bisiApp.repository.KullaniciRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BisiAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BisiAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner ornekIlanlariEkle(
            IlanRepository ilanRepository,
            KullaniciRepository kullaniciRepository
    ) {
        return args -> {
            if (ilanRepository.count() > 0) {
                return;
            }

            List<Kullanici> kullanicilar = kullaniciRepository.findAll();
            if (kullanicilar.isEmpty()) {
                return;
            }

            String[] basliklar = {
                    "Bianchi Oltre karbon yarış bisikleti",
                    "Carraro Big 927 dağ bisikleti",
                    "Trek Domane yol bisikleti",
                    "Scott Scale 980 performans dağ bisikleti",
                    "Kron RC 300 şehir bisikleti",
                    "Salcano XRS yarış bisikleti",
                    "Mosso Wildfire 29 jant",
                    "Carraro Gravel 012 uzun tur bisikleti",
                    "Bianchi Magma 29 hidrolik disk",
                    "Trek Marlin 7 temiz kullanım",
                    "Scott Addict hafif karbon kadro",
                    "Kron XC 150 genç performans bisikleti",
                    "Salcano NG 650 dayanıklı şehir bisikleti",
                    "Mosso Cavalier yol performans bisikleti",
                    "Carraro Force 930 bakım görmüş",
                    "Bianchi Sprint yarış odaklı",
                    "Trek FX hibrit performans bisikleti",
                    "Scott Sub Cross kondisyon bisikleti",
                    "Kron Fold katlanır şehir bisikleti",
                    "Salcano Astro çocuk performans bisikleti"
            };

            String[] kategoriler = {
                    "Yarış Bisikleti", "Dağ Bisikleti", "Yarış Bisikleti", "Dağ Bisikleti", "Şehir Bisikleti",
                    "Yarış Bisikleti", "Dağ Bisikleti", "Elektrikli", "Dağ Bisikleti", "Dağ Bisikleti",
                    "Yarış Bisikleti", "Dağ Bisikleti", "Şehir Bisikleti", "Yarış Bisikleti", "Dağ Bisikleti",
                    "Yarış Bisikleti", "Şehir Bisikleti", "Şehir Bisikleti", "Şehir Bisikleti", "Çocuk Bisikleti"
            };

            String[] markalar = {
                    "Bianchi", "Carraro", "Trek", "Scott", "Kron",
                    "Salcano", "Mosso", "Carraro", "Bianchi", "Trek",
                    "Scott", "Kron", "Salcano", "Mosso", "Carraro",
                    "Bianchi", "Trek", "Scott", "Kron", "Salcano"
            };

            String[] renkler = {
                    "Siyah", "Kırmızı", "Beyaz", "Mavi", "Gri",
                    "Turuncu", "Yeşil", "Siyah", "Kırmızı", "Mavi",
                    "Beyaz", "Gri", "Siyah", "Kırmızı", "Turuncu",
                    "Beyaz", "Yeşil", "Gri", "Siyah", "Mavi"
            };

            String[] durumlar = {
                    "Çok İyi", "İyi", "Sıfır Ayarında", "İyi", "Kullanılmış",
                    "Çok İyi", "Bakım Gerekli", "İyi", "Çok İyi", "Kullanılmış",
                    "Sıfır Ayarında", "İyi", "Kullanılmış", "Çok İyi", "İyi",
                    "Sıfır Ayarında", "İyi", "Kullanılmış", "Çok İyi", "İyi"
            };

            int[] yillar = {2023, 2022, 2024, 2021, 2020, 2023, 2019, 2022, 2024, 2021, 2025, 2020, 2018, 2023, 2022, 2025, 2021, 2020, 2022, 2019};
            int[] vitesler = {22, 18, 24, 20, 16, 24, 21, 10, 12, 18, 24, 18, 7, 22, 20, 24, 14, 16, 8, 6};
            BigDecimal[] fiyatlar = {
                    new BigDecimal("84500"), new BigDecimal("28900"), new BigDecimal("69900"), new BigDecimal("35400"),
                    new BigDecimal("17250"), new BigDecimal("41800"), new BigDecimal("24500"), new BigDecimal("56200"),
                    new BigDecimal("33900"), new BigDecimal("31400"), new BigDecimal("92500"), new BigDecimal("18600"),
                    new BigDecimal("12900"), new BigDecimal("38750"), new BigDecimal("27600"), new BigDecimal("81100"),
                    new BigDecimal("21400"), new BigDecimal("19800"), new BigDecimal("16750"), new BigDecimal("9800")
            };

            List<Ilan> ilanlar = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Ilan ilan = new Ilan();
                ilan.setBaslik(basliklar[i]);
                ilan.setKategori(kategoriler[i]);
                ilan.setMarka(markalar[i]);
                ilan.setRenk(renkler[i]);
                ilan.setModelYili(yillar[i]);
                ilan.setVitesSayisi(vitesler[i]);
                ilan.setDurum(durumlar[i]);
                ilan.setFiyat(fiyatlar[i]);
                ilan.setAciklama("Düzenli bakımı yapılmış, performans odaklı, ikinci el bisiklet. Kadro ve aktarma durumu temizdir. Detaylı bilgi için ilan sahibiyle iletişime geçebilirsiniz.");
                ilan.setResimUrl("https://placehold.co/900x700?text=Bisiklet+" + (i + 1));
                ilan.setSatici(kullanicilar.get(i % kullanicilar.size()));
                ilanlar.add(ilan);
            }

            ilanRepository.saveAll(ilanlar);
        };
    }
}
