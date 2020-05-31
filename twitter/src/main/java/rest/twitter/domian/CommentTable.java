package rest.twitter.domian;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class CommentTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="author")
    private long author;

    @Column(name="tweet")
    private long tweet;

    private String text;


    public CommentTable(){};

    public CommentTable(long author_, long tweet_,String text_){

        author=author_;
        tweet=tweet_;
        text=text_;
    }


}

