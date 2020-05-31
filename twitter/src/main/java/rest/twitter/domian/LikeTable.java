package rest.twitter.domian;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class LikeTable implements Serializable {
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
