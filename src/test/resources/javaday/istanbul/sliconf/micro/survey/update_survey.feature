# language: tr

@Anket
Özellik: Etkinliğe daha önceden eklenmiş bir anketi güncelleme

  Senaryo: AAB5 -  Etkinlik sahibi etkinliğindeki bir anketi güncellemek istiyor
    Diyelim ki Etkinlik sahibi sistemde mevcut, daha önceden oluşturmuş olduğu bir anketi güncellemek istiyor
    Eğer ki Etkinlik sahibi güncellenen anket için geçerli bir anket ismi vermiş ise
    Ve Güncellenen anketin başlangıç ve bitiş tarihi geçerli formatta ise
    Ve Güncellenen anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise
    Ve Güncellenen anketin bitiş tarihi başlangıç tarihinden önce değil ise
    Ve Güncellenen anket en az bir soru içeriyorsa
    Ve Güncellenen anketteki soruların isimleri geçerli ise
    Ve Güncellenen anketteki sorular en az iki şık içeriyorsa
    Ve Güncellenen anketteki soru şıklarının isimleri geçerli ise
    O zaman Sistem anketin bilgilerini günceller ve güncelleme başarılı şeklinde bir mesaj üretir