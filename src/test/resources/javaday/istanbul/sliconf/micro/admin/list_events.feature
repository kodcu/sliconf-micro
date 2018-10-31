# language: tr

@Admin
Özellik: Admin için sistemdeki etkinliklerin listelenmesi

  Senaryo: 1 - Admin sistemdeki etkinlikleri görüntüleme sayfasına girdiğinde gerçekleşecek öntanımlı durum
    Diyelim ki Admin sistemdeki etkinlikleri görüntülemek istiyor
    Eğer ki Admin herhangi bir listeleme filtresi seçmediyse
    O zaman Sistem aktif ve yaklaşmakta olan etkinlikleri bir sayfada 20 adet olacak şekilde sayfalar halinde Admine gösterir

    Eğer ki Admin filtre olarak bir sayfada gösterilecek etkinlik sayısını 5 vermiş ise
    Ve Sayfa numarası olarak 0 vermiş ise
    Ve Sistemde aktif ve yaklaşmakta olan toplam 50 etkinlik var ise
    O zaman Sistem aktif ve yaklaşmakta olan etkinlikleri bir sayfada 5 adet olacak şekilde 9 sayfa halinde Admine gösterir

  Senaryo: 2 - Admin sistemdeki etkinlikleri görüntülemek istiyor
    Diyelim ki Admin sistemdeki tüm etkinlikleri görüntülemek istiyor
    Eğer ki Admin listeleme filtresi olarak tüm etkinlikleri seçtiyse
    O zaman Sistem bütün etkinlikleri Admine gösterir

  Senaryo: 3 - Admin sistemdeki etkinlikleri görüntülemek istiyor
    Diyelim ki Admin sistemdeki aktif veya pasif yaklaşmakta olan tüm etkinlikleri görüntülemek istiyor
    Eğer ki Admin listeleme filtresi olarak aktif veya pasif ve yaklaşmakta olan etkinlikleri seçti ise
    O zaman Sistem aktif veya pasif yaklaşmakta olan tüm etkinlikleri Admine gösterir

  Senaryo: 4 - Admin sistemdeki etkinlikleri görüntülemek istiyor
    Diyelim ki Admin sistemdeki başarılı şekilde bitmiş olan tüm etkinlikleri görüntülemek istiyor
    Eğer ki Admin listeleme filtresi olarak bitmiş etkinlikleri seçti ise
    O zaman Sistem gerçekleşip bitmiş olan tüm etkinlikleri Admine gösterir

  Senaryo: 5 - Admin sistemdeki etkinlikleri görüntülemek istiyor
    Diyelim ki Admin sistemdeki başarılı şekilde bitmiş olan tüm etkinlikleri görüntülemek istiyor
    Eğer ki Admin listeleme filtresi olarak bitmiş etkinlikleri seçti ise
    O zaman Sistem gerçekleşip bitmiş olan tüm etkinlikleri Admine gösterir

  Senaryo: 6 - Admin sistemdeki etkinlikleri görüntülemek istiyor
    Diyelim ki Admin sistemdeki aktif olamadan tamamlanıp başarısız etkinlikleri görüntülemek istiyor
    Eğer ki Admin listeleme filtresi olarak başarısız olmuş etkinlikleri seçmiş ise
    O zaman Sistem gerçekleşmeden bitip başarısız olan tüm etkinlikleri Admine gösterir

  Senaryo: 7 - Admin sistemdeki etkinlikleri görüntülemek istiyor
    Diyelim ki Admin sistemdeki şu anda gerçekleşen etkinlikleri görüntülemek istiyor
    Eğer ki Admin listeleme filtresi olarak şu anda gerçekleşen etkinlikleri seçmiş ise
    O zaman Sistem şu anda gerçekleşen tüm etkinlikleri Admine gösterir

