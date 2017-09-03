# language: tr

#1 - etkinlik sahibi
#2 - soru soran katılımcı
#3 - oy veren katılımcı
#4 - oy veren katılımcı
#5 - etkinlik yöntercileri

Özellik: Cep telefonu kullanıcısı mevcut açılmış etkinliğe giriyor

  Senaryo: Y473 - Cep telefonu kullanıcısı mevcut açılmış etkinliğe giriş yapıyor
    Diyelim ki sistemde aktif bir etkinlik var
    Eğer ki kullanıcı etkinliği ismi ile aratıyor , örneğin : Java Day
    O zaman sistem açık ve o an aktif olan etkinliklerin hepsini sıralar
    Ve kullanıcı listelenen açık etkinliklerden bir tanesi seçerek giriş yapabilir

    Diyelim ki sistemde aktif bir etkinlik var
    Eğer ki kullanıcı etkinliği kodu ile giriş yapmak istiyor , örneğin : K891
    O zaman sistem K891 kodu ile sistem içerisinde açık ve aktif bir etkinlik arar
    Ve böyle bir etkinlik o an açık ve aktiftir ve kullanıcı direkt ilgili etkinliğe giriş yapar.



