package javaday.istanbul.sliconf.micro.user.util;

import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserHelper {

    public ResponseMessage checkUserRoleIs(String role) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseMessage responseMessage = new ResponseMessage(true, "", new Object());

        boolean isAuthenticated = Objects.nonNull(authentication) && Objects.nonNull(authentication.getAuthorities());
        boolean isAuthorized = isAuthenticated && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
        if (!isAuthorized) {
            responseMessage.setStatus(false);
            responseMessage.setMessage("You have no authorization to do this!");
            responseMessage.setReturnObject(new Object());
        }

        return responseMessage;
    }
}
