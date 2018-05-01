package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.User;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, String>,
        CrudRepository<User, String>  {
    List<User> findByUsername(String username);

    List<User> findUsersByEmail(String email);

    User findFirstByEmail(String email);

    User findUserByEmailEquals(String email);

    // @Query("SELECT * FROM users WHERE META(users).id != $2 AND username = $1 ")
    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND username = $1 AND META().id != $2")
    List<User> findByUsernameAndIdNot(String username, String id);

    List<User> findById(String id);

    List<User> findByIdNot(String id);

    void removeAllByEmailAndUsername(String email, String username);

    List<User> findByDeviceId(String deviceId);

    List<User> findByAnonymousOrAnonymous(Boolean anonymous, Boolean anonymous2);
}