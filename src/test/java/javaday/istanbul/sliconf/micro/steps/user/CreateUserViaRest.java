package javaday.istanbul.sliconf.micro.steps.user;


import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.user.controller.CreateUserRoute;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Ignore
public class CreateUserViaRest extends SpringBootTestConfig {  // NOSONAR

    RequestBuilder requestBuilder;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @Before
    public void init() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Diyelimki("^: Yeni kullanıcı mobil üzerinden kayıt oluyor$")
    public void yeniKullanıcıMobilÜzerindenKayıtOluyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // Rest API initilization

        // Given


        requestBuilder = MockMvcRequestBuilders.get(
                "/service/users/register").accept(
                MediaType.APPLICATION_JSON);

        ResultMatcher ok = MockMvcResultMatchers.status()
                .isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/users/register");
        this.mockMvc.perform(builder)
                .andExpect(ok);






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
