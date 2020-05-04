package rest.twitter.domian;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class LikeTable {
    @Id
    @GeneratedValue
    private long id;

    private long author;

    private long tweet;

    public LikeTable(){}

    public LikeTable(long author_, long tweet_){
        author=author_;
        tweet=tweet_;
    }
}
