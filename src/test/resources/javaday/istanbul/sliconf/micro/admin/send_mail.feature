# language: tr

@Mail
Özellik: Admin için sistemdeki etkinliklerin mail gonderilmesi

   Senaryo: 1 - Upcoming eventleri admine haftalık  mail gonderiyoruz
     Diyelim ki Yeni bir sablon olusturuluyor

   Senaryo:  2 - Upcoming eventleri admine haftalık  mail gonderiyoruz
     Diyelim ki Basarili mail gonderiyoruz
     Eğer ki Template null degilse
     O zaman Mail basarili sekilde gonderilir
  Senaryo:  3 - Upcoming eventleri admine haftalık  mail gonderiyoruz
    Diyelim ki Bos mail mesaji gonderiyoruz
    Eğer ki template null ise
    O zaman Admine template bos mesaji  gonderilir
  Senaryo: 4 - Complete olmuş eventleri admine mail gonderiyoruz
    Diyelim ki event basarili sekide kaydedildi
    Eğer ki template null degilse
    O zaman Admine kaydedilen event mail gonderilir
    Eğer ki Template null ise
    O zaman Admine template null maili gonderlir
  Senaryo: 5 - Complete olmamış eventleri admine mail gondermiyoruz
    Diyelim ki event basarili sekide kaydedilmedi
    O zaman Admine mail gonderilmez







