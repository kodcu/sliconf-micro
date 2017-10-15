package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.token.Token;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.PasswordResetService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.mail.GandiMailSendService;
import javaday.istanbul.sliconf.micro.service.token.TokenRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;


@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class K683 {

    @Autowired
    private EventRepositoryService eventRepositoryService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private TokenRepositoryService tokenRepositoryService;

    @Autowired
    private PasswordResetService passwordResetService;

    private User user;

    private String email1 = "osman15@osman.com";
    private boolean result1 = true;

    private String email2 = "osman123@osman.com";
    private boolean result2 = true;

    private String email3 = "osman.osman.com";
    private boolean result3 = false;

    private String email4 = "osman@osman";
    private boolean result4 = false;

    private String email5 = "osman@osman.com.tr";
    private boolean result5 = true;

    private String email6 = "osman_uykulu-1a@osman.com";
    private boolean result6 = true;

    private boolean isPassworForgetten = true;

    Token token;
    Token token1;
    Token token2;

    @Before
    public void init() {

        GandiMailSendService.isTest = true;

        token = tokenRepositoryService.generateToken("osman15@osman.com");

        /*
        token.setTokenValue("1234");
        token.setValidMail("osman15@osman.com");
        token.setValidUntilDate(LocalDateTime.now().plusMinutes(5));
        */

        token1 = tokenRepositoryService.generateToken("osman15@osman.com");

        /*
        token1.setTokenValue("12345");
        token1.setValidMail("osman15@osman.com");
        token1.setValidUntilDate(LocalDateTime.now().minusMinutes(5));
        */

        token2 = tokenRepositoryService.generateToken("osman15@osman.com");
        /*
        token2.setTokenValue("123456");
        token2.setValidMail("osman15@osman.com");
        token2.setValidUntilDate(null);
        */

        assertTrue(tokenRepositoryService.save(token));
        assertTrue(tokenRepositoryService.save(token1));
        assertTrue(tokenRepositoryService.save(token2));
    }


    @Diyelimki("^etkinlik sahibi daha önceden JugEvents sistemine kayıt olmuş$")
    public void etkinlikSahibiDahaÖncedenJugEventsSistemineKayıtOlmuş() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setPassword("1234!")
                .setEmail("osman15@osman.com")
                .build();

        assertNotNull(user);

        userRepositoryService.save(user);
    }

    @Eğerki("^etkinlik sahibi şifremi kaybettim çağrısı yapıyorsa$")
    public void etkinlikSahibiŞifremiKaybettimÇağrısıYapıyorsa() throws Throwable {
        assertTrue(isPassworForgetten);
    }

    @Ozaman("^sistem etkinlik sahibinden sisteme kayıtlı eposta adresini ister ve gecerliligini kontrol eder$")
    public void sistemEtkinlikSahibindenSistemeKayıtlıEpostaAdresiniIsterVeGecerliliginiKontrolEder() throws Throwable {

        assertEquals(result1, passwordResetService.checkIsMailValid(email1));

        assertEquals(result2, passwordResetService.checkIsMailValid(email2));

        assertEquals(result3, passwordResetService.checkIsMailValid(email3));

        assertEquals(result4, passwordResetService.checkIsMailValid(email4));

        assertEquals(result5, passwordResetService.checkIsMailValid(email5));

        assertEquals(result6, passwordResetService.checkIsMailValid(email6));

    }

    @Ozaman("^etkinlik sahibinin eposta adresine şifre değiştirme bağlantısı gönderilir$")
    public void etkinlikSahibininEpostaAdresineŞifreDeğiştirmeBağlantısıGönderilir() throws Throwable {
        assertEquals(result1, userRepositoryService.controlIfEmailIsExists(email1));


        ResponseMessage responseMessage = passwordResetService.sendPasswordResetMail(email1);

        assertNotNull(responseMessage);

        assertTrue(responseMessage.isStatus());
    }

    @Ozaman("^etkinlik sahibi şifresini değiştirerek sisteme giriş yapabilir\\.$")
    public void etkinlikSahibiŞifresiniDeğiştirerekSistemeGirişYapabilir() throws Throwable {
        String newPass1 = "password1";
        String newPass1_again = "password1";

        String newPass2 = "pass2";
        String newPass2_again = "pass21";

        String newPass3 = "pass3";
        String newPass3_again = "pass3";


        ResponseMessage responseMessageResult = tokenRepositoryService.isTokenValid(token.getTokenValue());
        assertNotNull(responseMessageResult);
        assertTrue(responseMessageResult.isStatus());


        ResponseMessage changePassResponse1_result = userRepositoryService.changePassword(
                (String) responseMessageResult.getReturnObject(), newPass1, newPass1_again);

        assertNotNull(changePassResponse1_result);
        assertTrue(changePassResponse1_result.isStatus());


        ResponseMessage changePassResponse2_result = userRepositoryService.changePassword(
                (String) responseMessageResult.getReturnObject(), newPass2, newPass2_again);

        assertNotNull(changePassResponse2_result);
        assertFalse(changePassResponse2_result.isStatus());

        ResponseMessage changePassResponse3_result = userRepositoryService.changePassword(
                (String) responseMessageResult.getReturnObject(), newPass3, newPass3_again);

        assertNotNull(changePassResponse3_result);
        assertFalse(changePassResponse3_result.isStatus());


        ResponseMessage responseMessageResult2 = tokenRepositoryService.isTokenValid("notValidToken");
        assertNotNull(responseMessageResult2);
        assertFalse(responseMessageResult2.isStatus());

        ResponseMessage responseMessageResult3 = tokenRepositoryService.isTokenValid(token2.getTokenValue());
        assertNotNull(responseMessageResult3);
        assertTrue(responseMessageResult3.isStatus());

    }
}
