package javaday.istanbul.sliconf.micro.user.util;

import com.devskiller.jfairy.Fairy;
import javaday.istanbul.sliconf.micro.user.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserGenerator {

    private UserGenerator() {}

    /**
     *
     * @param count Olusturulacak kullanici sayisi
     * @return Geriye kullanici seti dondurur.
     */
    public static Set<User> generateRandomUsers(int count) {

        Fairy fairy = Fairy.create();
        Set<User> users = new HashSet<>();
        for (int i = 0; i < count; i++) {
            User user = new UserBuilder()
                    .setUserName(fairy.person().getUsername())
                    .setName(fairy.person().getFirstName())
                    .setFullName(fairy.person().getFullName())
                    .setEmail(fairy.person().getEmail())
                    .setPassword(fairy.person().getPassword())
                    .build();
            users.add(user);
        }
        return users;

    }

}
