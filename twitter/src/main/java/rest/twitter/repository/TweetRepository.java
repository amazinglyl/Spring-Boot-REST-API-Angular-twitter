package rest.twitter.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import rest.twitter.domian.*;

import java.util.List;


public interface TweetRepository extends JpaRepository<Tweet,Long> {
    Tweet findByIdAndDisable(long id,boolean disable);
    List<Tweet> findByAuthorAndDisable(long author,boolean disable);
}