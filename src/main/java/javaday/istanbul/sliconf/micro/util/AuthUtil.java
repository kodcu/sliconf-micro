package javaday.istanbul.sliconf.micro.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;

/**
 * Sosyal medya butonlari kullanilarak giris yapilmasini saglayan utility sinifi
 * Apiler ile iletisime gecip frontend uzerinden gelen acces token lar sayesinde kullanici bilgilerini alan
 * metodlari barindirir.
 */
public class AuthUtil {

    private static Logger logger = LoggerFactory.getLogger(AuthUtil.class);

    private static final String GOOGLE_CLIENT_ID = "31237231524-4vibq7hrr7g6dsp1h9oh5h9k9mmndhhq.apps.googleusercontent.com";
    private static final String LINKED_IN_CLIENT_ID = "863g25szn8vggb";

    private static final GsonFactory gsonFactory = new GsonFactory();
    private static final HttpTransport transport = new NetHttpTransport();

    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
            .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
            .build();

    public static final String SERVICE_GOOGLE = "google";
    public static final String SERVICE_LINKEDIN = "linkedin";

    private AuthUtil() {
        // Private for static access
    }

    /**
     * Frontend tafarindan saglanan token id ile google apisi uzerinden kullanici bilgilerine erisir
     * Eger kullanici bilgisi alindi ise bu bilgilerle {User} olusturur eger bilgiler bir sekilde alinamadiysa
     * {null} doner
     * @param idTokenString
     * @return
     */
    public static User loginGoogle(String idTokenString) {
        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = null;

        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            logger.error("Security exception while logging with google", e);
        } catch (IOException e) {
            logger.error("IO exception while logging with google", e);
        }

        if (Objects.nonNull(idToken)) {
            Payload payload = idToken.getPayload();

            // Get profile information from payload
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            return new UserBuilder()
                    .setName("user-" + email)
                    .setFullName(name)
                    .setEmail(email)
                    .build();
        } else {

            logger.error("Invalid ID token while login with google", idTokenString);
            return null;
        }
    }

    /**
     * Todo henuz linkedin duzgun calismiyor. Linkedin access tokeni kabul etmiyor
     * @param token
     * @return
     */
    public static User loginLinkedIn(String token) {
        if (Objects.isNull(token)) {
            return null;
        }

        String url = "https://api.linkedin.com/v1/people/~?format=json" + "&client_id=" + LINKED_IN_CLIENT_ID;

        HttpURLConnection con = null;

        try {

            URL myUrl = new URL(url);
            con = (HttpURLConnection) myUrl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + token);

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            String contentStr = content.toString();

            logger.info("LinkedIn login: %s", contentStr);

        } catch (ProtocolException e) {
            logger.error("LinkedIn ProtocolException: ", e);
        } catch (MalformedURLException e) {
            logger.error("LinkedIn MalformedURLException: ", e);
        } catch (IOException e) {
            logger.error("LinkedIn IOException: ", e);
        } finally {
            if (Objects.nonNull(con))
                con.disconnect();
        }

        return null;
    }

}
