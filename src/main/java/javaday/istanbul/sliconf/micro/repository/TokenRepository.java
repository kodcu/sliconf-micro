package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.token.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    Token findTokenByTokenValueEquals(String tokenValue);

}