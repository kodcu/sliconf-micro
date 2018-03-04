package javaday.istanbul.sliconf.micro.security;

import com.hazelcast.core.IMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.token.SecurityToken;
import javaday.istanbul.sliconf.micro.provider.DistributedMapProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class TokenAuthenticationService {

    //    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final long EXPIRATION_TIME = 1; // days
    private static final ChronoUnit expirationUnit = ChronoUnit.DAYS; // days
    private static final TimeUnit expirationTimeUnit = TimeUnit.DAYS; // days
    private static final String SECRET = "ThisIsASecret";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    @Autowired
    private DistributedMapProvider distributedMapProvider;

    @Autowired
    public TokenAuthenticationService(DistributedMapProvider distributedMapProvider) {
        this.distributedMapProvider = distributedMapProvider;
    }

    public void addAuthentication(HttpServletResponse res, User user) {
        Date date = Date.from(Instant.now(Clock.system(
                ZoneId.of("Asia/Istanbul")
                )
                ).plus(EXPIRATION_TIME, expirationUnit)
        );

        String jwt = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .claim("user", user)
                .claim("date", date)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        SecurityToken securityToken = new SecurityToken();

        securityToken.setUsername(user.getUsername());
        securityToken.setRole(user.getRole());
        securityToken.setUser(user);
        securityToken.setValidUntilDate(date);

        distributedMapProvider.putSecurityToken("securityTokens", user.getUsername(),
                securityToken, EXPIRATION_TIME, expirationTimeUnit);

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (Objects.nonNull(token)) {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);
            Date date = claims.get("date", Date.class);
            Object user = claims.get("user", Object.class);

            IMap<String, SecurityToken> securityTokenMap = distributedMapProvider.getSecurityTokenMap("securityTokens");

            if (Objects.nonNull(securityTokenMap) && !securityTokenMap.isEmpty()) {
                SecurityToken securityToken = securityTokenMap.get(username);

                if (Objects.nonNull(securityToken) && Objects.nonNull(securityToken.getValidUntilDate()) &&
                        Objects.nonNull(date) && !date.after(securityToken.getValidUntilDate())) {
                    return new UsernamePasswordAuthenticationToken(username, user, AuthorityUtils.createAuthorityList(role));
                }
            }
        }

        return null;
    }
}