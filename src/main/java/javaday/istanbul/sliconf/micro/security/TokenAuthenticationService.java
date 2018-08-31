package javaday.istanbul.sliconf.micro.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hazelcast.core.IMap;
import io.jsonwebtoken.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.token.SecurityToken;
import javaday.istanbul.sliconf.micro.provider.DistributedMapProvider;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Component
public class TokenAuthenticationService {

    //    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final long EXPIRATION_TIME = 1; // days
    private static final ChronoUnit expirationUnit = ChronoUnit.DAYS; // days
    private static final TimeUnit expirationTimeUnit = TimeUnit.DAYS; // days
    // TODO: 27.08.2018 Proje github da oldugundan bu secretin buradan kaldirilmasi gerekebilir.?
    private static final String SECRET = "0MLYpRduz8m10E1eMyUYczc7xBp13nDc";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";
    private static final String SECURITY_TOKENS = "securityTokens";

    private final UserRepositoryService userRepositoryService;
    private final DistributedMapProvider distributedMapProvider;




    public String addAuthentication(HttpServletResponse res, User user) {
        Date date = Date.from(Instant.now(Clock.system(
                ZoneId.of("Asia/Istanbul")
                )
                ).plus(EXPIRATION_TIME, expirationUnit)
        );

        SecurityToken securityToken = getSecurityTokenFromMap(user);

        if (Objects.isNull(securityToken)) {
            securityToken = new SecurityToken();

            securityToken.setUsername(user.getUsername());
            securityToken.setRole(user.getRole());
            securityToken.setUser(user);
            securityToken.setValidUntilDate(date);

            distributedMapProvider.putSecurityToken(SECURITY_TOKENS, user.getUsername(),
                    securityToken, EXPIRATION_TIME, expirationTimeUnit);
        }

        String jwt = generateJwtString(user, date);

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);

        return TOKEN_PREFIX + " " + jwt;
    }

    private SecurityToken getSecurityTokenFromMap(User user) {
        IMap<String, SecurityToken> securityTokenMap = distributedMapProvider.getSecurityTokenMap(SECURITY_TOKENS);

        if (Objects.nonNull(securityTokenMap) && !securityTokenMap.isEmpty()) {
            return securityTokenMap.get(user.getUsername());
        }

        return null;
    }

    public String generateJwtString(User user, Date date) {
        if (Objects.nonNull(date) && isUserFieldsAreNotNull(user)) {
            return Jwts.builder()
                    .claim("username", user.getUsername())
                    .claim("role", user.getRole())
                    .claim("user", user)
                    .claim("date", date)
                    .setExpiration(date)
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();
        }

        return null;
    }

    private boolean isUserFieldsAreNotNull(User user) {
        return Objects.nonNull(user) && Objects.nonNull(user.getUsername()) &&
                Objects.nonNull(user.getRole());
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (Objects.nonNull(token)) {

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody();

                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);
                Date date = claims.get("date", Date.class);
                Object user = claims.get("user", Object.class);

                IMap<String, SecurityToken> securityTokenMap = distributedMapProvider.getSecurityTokenMap(SECURITY_TOKENS);
//                JsonObject jsonObject;
//                Gson gson = new Gson();
//                try {
//                   BufferedReader bufferedReader = request.getReader();
//                    jsonObject = gson.fromJson(bufferedReader, JsonObject.class);
//
//               } catch (IOException e) {
//                   return null;
//               }
//                String userIdFromRequest;
//                String userIdFromToken;
//                if(!Objects.nonNull(jsonObject)) {
//                    // TODO: 28.08.2018 dinamik bir sekilde yap.
//                    userIdFromRequest = "5b72dc350662c13c3378411f";
//                }
//                else
//                    userIdFromRequest = jsonObject.get("userId").toString();
//                userIdFromToken = userRepositoryService.findByUsername(username).get(0).getId();

                    if (Objects.nonNull(securityTokenMap) && !securityTokenMap.isEmpty()) {
                        SecurityToken securityToken = securityTokenMap.get(username);

                        if (Objects.nonNull(securityToken) && Objects.nonNull(securityToken.getValidUntilDate()) &&
                                Objects.nonNull(date) && !date.before(securityToken.getValidUntilDate())) {
                            return generateAuthentication(username, role, user);
                        }
                    }


            } catch (ExpiredJwtException | UnsupportedJwtException |
                    MalformedJwtException | SignatureException | IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    public Authentication generateAuthentication(String username, String role, Object user) {
        return new UsernamePasswordAuthenticationToken(username, user, AuthorityUtils.createAuthorityList(role));
    }
}