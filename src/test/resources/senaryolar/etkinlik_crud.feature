# language: tr

#1 - etkinlik sahibi
#2 - soru soran katılımcı
#3 - oy veren katılımcı
#4 - oy veren katılımcı
#5 - etkinlik yöntercileri

Özellik: Yeni Etkinlik oluşturma web versiyonu

  Senaryo: F633 - Sistemle yeni tanışmış çiçeği burnunda etkinlik sahibi etkinlik oluşturuyor, heyecan dorukta
    Diyelim ki potansiyel etkinlik sahibi JugEvents sisteminde yeni bir etkinlik açmak istedi
    Eğer ki potansyel etkinlik sahibi,  ad, soyad, eposta ve şifre bilgisini eksiksiz vermişse
    Ve eposta adresi sistemde daha önceden kayıtlı değilse
    Ve etkinliği adı asgari düzeyde yeterliyse - min 4 harf ise
    Ve etkinliğin tarihi bugün veya daha ileri bir tarih olarak belirtlişse
    O zaman sistem etkinlik sahibini kayıt eder ve etkinlik oluşturulmuş olur
    Ve sistem etkinliğe özel ve eşşiz bir etkinlik kodu üretir.


  Senaryo: 6793 - Sistemle aynı eposta adresi ile yineden hesap açmak isterken neler yaşanmalı
    Diyelim ki potansiyel etkinlik sahibi JugEvents sisteminde yeni bir etkinlik açmak istedi
    Eğer ki potansyel etkinlik sahibi,  ad, soyad, eposta ve şifre bilgisini eksiksiz vermişse
    Ama eposta adresi sistemde daha önceden kayıtlı ise
    O zaman sistem potansiyel etkinlik sahibini çelişkiyi haber verir.



  Senaryo: 8685 - Sisteme daha evvelden kayıtlı bir etkinlik sahibinin sisteme girişi
    Diyelim ki  etkinlik sahibi daha önceden JugEvents sistemine başarılı bir şekilde kayıt olmuş
    Eğer ki etkinlik sahibi,  kulanıcı adı ve şifresini doğru bir şekilde girerse
    O zaman sistem etkinlik sahibininin açmış olduğu aktif ve/veya pasif etkinlikleri getirmeli.

  Senaryo: B768 - Kayıtlı kullanıcının etkinliklerini listeliyor
    Diyelim ki  etkinlik sahibi sisteme başarılı bir şekilde giriş yaptı, herşey yolunda
    Eğer ki etkinlik sahibi etkinliklerini listelemek isterse
    O zaman sistem etkinlik sahibinin geçmiş ve gelecek tüm etkinliklerini listeler


    Senaryo: A265 - Kayıtlı kullanıcının ayarlardan bilgilerini değiştirmek istiyor
      Diyelim ki etkinlik sahibi ismini değiştimek istiyor
      Eğer ki yeni ismi asgari 3 harf girilmişse
      O zaman sistem isim değişikliğe izin verir

      Diyelim ki etkinlik sahibi şifresini değiştimek istiyor
      Eğer ki etkinlik sahibi şifresini asgari 3 harf girmişse
      O zaman sistem şifre değişikliğe izin verir

      Diyelim ki etkinlik sahibi eposta epostasını değiştirmek istiyor
      O zaman sistem eposta değişikliğe izin verir vermemeli




  Senaryo: K683 - Şifresini kayıp etmiş etkinlik sahibi
    Diyelim ki  etkinlik sahibi daha önceden JugEvents sistemine kayıt olmuş
    Eğer ki etkinlik sahibi şifremi kaybettim çağrısı yapıyorsa
    O zaman sistem etkinlik sahibinden sisteme kayıtlı eposta adresini ister
    Ve etkinlik sahibinin eposta adresine şifre değiştirme bağlantısı gönderilir
    Ve etkinlik sahibi şifresini değiştirerek sisteme giriş yapabilir.


  Senaryo: Z926 - Etkinlik sahibi yeni bir etkinlik daha açmak istiyor.
    Diyelim ki etkinlik sahibi daha önceden JugEvents sistemine kayıtlıdır
    Eğer ki etkinlik sahibi yeni bir etkinliği etkinlik kurallarına uygun bir şekilde girerse
    O zaman sistem etkinlik sahibinin yeni etkinliğini başarılı bir şekilde oluşturur.