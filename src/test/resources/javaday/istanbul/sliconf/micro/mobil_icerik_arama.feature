# language: tr

#1 - etkinlik sahibi
#2 - soru soran katılımcı
#3 - oy veren katılımcı
#4 - oy veren katılımcı
#5 - etkinlik yöntercileri

Özellik: Mobil kullanıcısı mevcut açılmış etkinliğe içinde arama ve filtreleme yapıyor

  Senaryo: N882 - Mobil kullanıcı arama yapıyor ve tüm başlangıç seviyesindeki konuşmalar geliyor
    Diyelim ki sistemde aktif bir etkinlik var
    Eğer ki kullanıcı arama kriteri olarak micro yazdı
    Ve  orta düzey konuşmacıları seçerse
    O zaman sistem ilgili etkinlikten alakalı sonuçları döndürmelidir.

    Diyelim ki sistemde aktif bir etkinlik var
    Eğer ki kullanıcı arama kriteri olarak orta yazıp arama yapmak isterse
    O zaman sistem orta seviye konuşmaları da getirmelidir.

    Diyelim ki sistemde aktif bir etkinlik var
    Eğer ki kullanıcı arama kriterini  boş bırakırsa
    Ve sadece orta seviyeyi konuşmaları aramak isterse
    O zaman sistem orta seviye konuşmaların hepsini direkt getirlidir.

