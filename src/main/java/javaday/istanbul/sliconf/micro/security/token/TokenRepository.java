package javaday.istanbul.sliconf.micro.security.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository extends MongoRepository<Token, String>,
        CrudRepository<Token, String> {

    Token findTokenByTokenValueEquals(String tokenValue);

}