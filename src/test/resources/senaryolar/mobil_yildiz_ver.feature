# language: tr

#1 - etkinlik sahibi
#2 - soru soran katılımcı
#3 - oy veren katılımcı
#4 - oy veren katılımcı
#5 - etkinlik yöntercileri

Özellik:  Oturum içinde ki konuşmalara yildiz verilmesi

  Senaryo: F967 - Mobilden bağlanan kullanınıcı konuşmaya yıldız veriyor.

    Diyelim ki kayıtlı kullanıcı mevcut aktif bir etkinlikteki bir oturuma girdi.
    Eğer ki 5 üzerinde 1 yıldız vermişse
    Ve oturumun toplam oy verenlerin sayısı : 10, yıldız ortalaması 4,5 ise
    O zaman  yıldız ortalaması 4,1 olur ve aşağı doğru yuvarlanırsa 4 olmaya devam eder


    Diyelim ki kayıtlı kullanıcı mevcut aktif bir etkinlikteki bir oturuma girdi.
    Eğer ki 5 üzerinde 1 yıldız vermişse
    Ve an saat itbariyle oturum saat olarak henüz başlamamış ise
    O zaman  sistem kullanıcıyı nazikce konu hakkında uyarmalıdır.

