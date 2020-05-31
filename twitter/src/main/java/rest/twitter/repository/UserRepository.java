package rest.twitter.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import rest.twitter.domian.*;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    long count();
    List<User> findByName(String name);
}
