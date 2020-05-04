package rest.twitter.repository;


import rest.twitter.domian.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommmetRepository extends JpaRepository<CommentTable, Long> {
    List<CommentTable> findByTweet(long tweet);
}
