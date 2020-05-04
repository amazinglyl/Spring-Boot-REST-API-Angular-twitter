package rest.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rest.twitter.domian.*;

import java.util.List;

public interface LikeTableRepository  extends JpaRepository<LikeTable, Long> {
    List<LikeTable> findByTweet(long tweet);
}
