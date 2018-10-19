# language: tr

@Anket
Özellik: Anketin cevaplanması

  Senaryo: AAB6 -  Kullanıcı etkinlikteki bir ankete cevap veriyor
    Diyelim ki Kullanıcı etkinlikteki bir ankete cevap vermek istiyor
    Eğer ki Kullanıcı sistemde mevcut olan etkinlikteki bir ankete cevap vermek istiyor ise
    Ve Kullanıcı ankete daha önceden cevap vermemiş ise
    Ve En az 1 adet soruya cevap içeriyor ise
    Ve Verilen her cevap ankette bir soruya karşılık geliyor ise
    O zaman Anketin katılanların sayısı güncellenir
    Ve Anketin sorularının cevaplanma sayıları güncellenir
    Ve Anketteki soruların seçeneklerinin seçilme sayısı güncellenir
    O zaman Sistem cevabı kaydeder


  Senaryo: AAB7 - Kullanıcı etkinlikteki bir ankete cevap veriyor
    Diyelim ki Kullanıcı etkinlikteki bir ankete cevap vermek istiyor
    Eğer ki Kullanıcı sistemde mevcut olan etkinlikteki bir ankete cevap vermek istiyor ise
    Fakat Kullanıcı ankete daha önceden cevap vermiş ise
    O zaman Sistem cevabı kayıt etmez ve kullanıcıya bu anketi daha önce cevapladınız gibi bir uyarı mesajı gönderir

  Senaryo: AAB8 - Kullanıcı etkinlikteki bir ankete cevap veriyor
    Diyelim ki Kullanıcı etkinlikteki bir ankete cevap vermek istiyor
    Eğer ki Kullanıcı sistemde mevcut olan etkinlikteki bir ankete cevap vermek istiyor ise
    Ve Kullanıcı ankete daha önceden cevap vermemiş ise
    Fakat En az 1 adet soruya cevap içermiyor ise
    O zaman Sistem cevabı kayıt etmez ve kullanıcıya en az 1 soru cevaplanmalı şeklinde bir uyarı mesajı gönderir

  Senaryo: AAB6 -  Kullanıcı etkinlikteki bir ankete cevap veriyor
    Diyelim ki Kullanıcı etkinlikteki bir ankete cevap vermek istiyor
    Eğer ki Kullanıcı sistemde mevcut olan etkinlikteki bir ankete cevap vermek istiyor ise
    Ve Kullanıcı ankete daha önceden cevap vermemiş ise
    Ve En az 1 adet soruya cevap içeriyor ise
    Fakat Verilen her cevap ankette bir soruya karşılık gelmiyor ise
    O zaman Sistem cevabı kayıt etmez ve kullanıcıya cevapların sorularla uyumsuzluğu ile alakalı bir mesaj yollanır