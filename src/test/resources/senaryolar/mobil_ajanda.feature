# language: tr

#1 - etkinlik sahibi
#2 - soru soran katılımcı
#3 - oy veren katılımcı
#4 - oy veren katılımcı
#5 - etkinlik yöntercileri

Özellik: Cep telefonu kullanıcısı ajandayı inceliyor

  Senaryo: N833 - Cep telefonu kullanıcısı 2 günlük bir etkinliğin ajandasını inceliyor
    Diyelim ki kullanıcı mevcut aktif bir etkinliğe üye olmadan giriş yaptı
    Eğer ki ajandaya bakmak isterse
    O zaman sistem ajanda bilgilerini getirir

    Diyelim ki kullanıcı mevcut aktif bir etkinliğe üye olmadan giriş yaptı
    Eğer ki kullanıcı katılmak istediği oturumları seçmek isterse
    O zaman sistem kayıt olmamış kullanıcının oturun kaydına izin vermemeli

    Diyelim ki kullanıcı mevcut aktif bir etkinliğe üye olmuş ise , kişisel bilgilerini vermiş ise
    Eğer ki kullanıcı katılmak istediği oturumları seçmek isterse
    O zaman sistem ilgili kullanıcının seçmiş olduğu oturumları kayıt eder.


  Senaryo: R872 - Kayıtlı kullanıcının kayıt olduğu oturumların listelenmesi
    Diyelim ki sistemde kayıtlı bir kullanıcı var
    Eğer ki bu kayıtlı kullanıcının kayıt olduğu oturumlar sorugulanmak isternirse
    O zaman sistem kayıtlı kullanıcının tüm kayıt olduğu oturumları dökmeli