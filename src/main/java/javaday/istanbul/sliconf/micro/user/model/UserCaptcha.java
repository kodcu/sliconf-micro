package javaday.istanbul.sliconf.micro.user.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ttayfur on 19/12/17.
 * <p>
 * Login sirasinda robot kontrolunde kullanilan captca nesnesi
 */
@Getter
@Setter
public class UserCaptcha {

    private String username;
    private String password;
    private String captcha;

    public UserCaptcha() {
        // for instantiation
    }
}
