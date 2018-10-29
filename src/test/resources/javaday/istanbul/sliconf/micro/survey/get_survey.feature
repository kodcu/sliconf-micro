# language: tr

@Anket
Özellik: Etkinlikteki bir anketin bilgilerine erişme

  Senaryo: AAB0 -  Bir anketin bilgilerini görüntüleme
    Diyelim ki Kullanıcı bir etkinlikteki bir anketin bilgilerine erişmek istiyor
    Eğer ki Kullanıcı sistemde mevcut olan bir etkinlikteki bir anketin bilgilerine erişmek istiyor ise
    Ve Kullanıcı sistemde mevcut olan bir anketin bilgilerine erişmek istiyor ise
    O zaman Sistem kullanıcıya istediği anketin bilgilerini gönderir

  Senaryo: AAB1 - Bir anketin bilgilerini görüntüleme
    Diyelim ki Kullanıcı bir etkinlikteki bir anketin bilgilerine erişmek istiyor
    Eğer ki Kullanıcı sistemde mevcut olmayan bir etkinlikteki bir anketin bilgilerine erişmek istiyor ise
    O zaman Sistem kullanıcıya böyle bir etkinliğin sistemde mevcut olmadığına dair bir uyarı mesajı gönderir

  Senaryo: AAB2 - Bir anketin bilgilerini görüntüleme
    Diyelim ki Kullanıcı bir etkinlikteki bir anketin bilgilerine erişmek istiyor
    Eğer ki Kullanıcı sistemde mevcut olan bir etkinlikteki bir anketin bilgilerine erişmek istiyor ise
    Fakat Kullanıcı sistemde mevcut olmayan bir anketin bilgilerine erişmek istiyor ise
    O zaman Sistem kullanıcıya böyle bir anketin sistemde mevcut olmadığına dair bir uyarı mesajı gönderir