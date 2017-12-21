package javaday.istanbul.sliconf.micro.util;

import com.google.gson.JsonObject;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VerifyCaptcha {

    public static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET_KEY = "6Le6PD0UAAAAAOADznI_FYrNWxzdb3LnDPOMq62H";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String SITE_KEY = "6Le6PD0UAAAAAP3JH2yxy18pEbGU8h5KwdY7yjXp"; // NOSONAR

    private static Logger logger = LoggerFactory.getLogger(VerifyCaptcha.class);

    private VerifyCaptcha() {
        // for static access
    }

    public static boolean verify(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }

        try {
            URL obj = new URL(URL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String postParams = "secret=" + SECRET_KEY + "&response="
                    + gRecaptchaResponse;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            logger.info("Sending 'POST' request to URL : %s", URL);
            logger.info("Post parameters : %s", postParams);
            logger.info("Response Code : %d", responseCode);


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();

            // print result
            logger.info(responseString);

            //parse JSON response and return 'success' value
            JsonObject jsonObject = JsonUtil.fromJson(responseString, JsonObject.class);

            return jsonObject.getAsJsonPrimitive("success").getAsBoolean();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}