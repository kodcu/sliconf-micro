package javaday.istanbul.sliconf.micro.steps.user;


import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.user.model.User;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.RequestBuilder;

import java.net.URI;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
public class CreateUserViaRest extends SpringBootTestConfig {  // NOSONAR


    RequestBuilder requestBuilder;

    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate restTemplate;


    @Diyelimki("^: Yeni kullanıcı mobil üzerinden kayıt oluyor$")
    public void yeniKullanıcıMobilÜzerindenKayıtOluyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // Rest API initilization

        // Given

        String user = "{email: \"altuga@ymail.com\", username: \"turkkahvesi\", password: \"upux1234\"}";
        HttpEntity<String> request = new HttpEntity<>(user);
        ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:" + port + "/service/users/register", HttpMethod.POST, request, String.class);



        //URI location = this.restTemplate.postForLocation("http://localhost:" + port + "/service/users/register", request);
        assertThat(200 , is(response.getStatusCode().value()));



    }

    @Eğerki("^: kullannıcı bilgileri iş kurallarına uygunsa$")
    public void kullannıcıBilgileriIşKurallarınaUygunsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // Burada new User demek lazım, yada String bir ifade ile oluşturmak lazım;
    }

    @Ozaman("^: Kullanıcı başarılı bir şekilde kayıt olur\\.$")
    public void kullanıcıBaşarılıBirŞekildeKayıtOlur() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // Burada rest çağrısıyla , kullanıcı bilgilerini gönderme ve
        // sonucu almayı bekliyorum.


    }


}
