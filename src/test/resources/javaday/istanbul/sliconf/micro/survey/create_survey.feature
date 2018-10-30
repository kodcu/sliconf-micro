# language: tr

@Anket
Özellik: Etkinliğe yeni bir anket ekleme

  Senaryo: AAA1 -  Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Ve Anketin bitiş tarihi başlangıç tarihinden önce değil ise
    Ve Anket en az bir soru içeriyorsa
    Ve Anketteki soruların isimleri geçerli ise
    Ve Anketteki sorular en az iki şık içeriyorsa
    Ve Anketteki soru şıklarının isimleri geçerli ise
    O zaman Sistem anketi kayıt eder ve etkinlik sahibi anketi oluşturmuş olur

  Senaryo: AAA2 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Fakat Etkinlik sahibi anket ismini boş bırakmış ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa anket ismi boş olamaz gibi bir hata mesajı gönderilir

#    Eğer ki Etkinlik sahibi anket ismini asgari 4 karakterten olan sınırdan az girmiş ise
#    O zaman Sistem anketi kayıt etmez ve ön tarafa anket ismi asgari 4 karakter olabilir gibi bir hata mesajı gönderilir
#
#    Eğer ki Etkinlik sahibi anket ismini azami 25 karakterten olan sınırdan fazla girmiş ise
#    O zaman Sistem anketi kayıt etmez ve ön tarafa anket ismi azami 25 karakter olabilir gibi bir hata mesajı gönderilir
#
#    Eğer ki Etkinlik sahibi anket isminde soru işareti gibi uygun olmayan karakterler kullandı ise
#    O zaman Sistem anketi kayıt etmez ve ön tarafa geçersiz format gibi bir hata mesajı gönderilir

  Senaryo: AAA3 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Fakat Anketin başlangıç tarihi uygun formatta değil ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa hatali başlangıç tarihi gibi bir hata mesajı gönderilir

    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Fakat Anketin bitiş tarihi uygun formatta değil ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa hatali bitiş tarihi gibi bir hata mesajı gönderilir

  Senaryo: AAA4 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Fakat Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmemiş ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa başlangıç tarihi yanlış gibi bir hata mesajı gönderilir


  Senaryo: AAA5 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Fakat Anketin bitiş tarihi başlangıç tarihinden önce ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa hatalı başlangıç-bitiş tarihi gibi bir hata mesajı gönderilir


  Senaryo: AAA6 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Ve Anketin bitiş tarihi başlangıç tarihinden önce değil ise
    Fakat Anket en az bir soru içermiyor ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa ankette en az bir soru olmalı gibi bir hata mesajı gönderilir

  Senaryo: AAA7 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Ve Anketin bitiş tarihi başlangıç tarihinden önce değil ise
    Ve Anket en az bir soru içeriyorsa
    Fakat Etkinlik sahibi anketteki sorularin ismini boş bırakmış ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa soruların isimleri boş olamaz gibi bir hata mesajı gönderilir

  Senaryo: AAA8 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Ve Anketin bitiş tarihi başlangıç tarihinden önce değil ise
    Ve Anket en az bir soru içeriyorsa
    Ve Anketteki soruların isimleri geçerli ise
    Fakat Anketteki sorular en az iki şık içermiyor ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa sorular en az 2 şık içermeli gibi bir hata mesajı gönderilir

  Senaryo: AAA9 - Etkinlik sahibi etkinlik genelinde bir anket oluşturmak istiyor
    Diyelim ki Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor
    Eğer ki Etkinlik sahibi geçerli bir anket ismi vermiş ise
    Ve Anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Ve Anketin bitiş tarihi başlangıç tarihinden önce değil ise
    Ve Anket en az bir soru içeriyorsa
    Ve Anketteki soruların isimleri geçerli ise
    Ve Anketteki sorular en az iki şık içeriyorsa
    Fakat Anketteki soru şıklarının isimleri geçerli değil ise
    O zaman Sistem anketi kayıt etmez ve ön tarafa şıkların isimleri boş gibi bir hata mesajı gönderilir