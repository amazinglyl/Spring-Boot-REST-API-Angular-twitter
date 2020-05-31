package rest.twitter.domian;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Tweet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="author")
    private long author;

    @Column(name="text")
    private String text;

    @Column(name="comments")
    private long comments;

    @Column(name="likes")
    private long likes;

    private boolean disable;

    private long visit;

    public Tweet(){};

    public Tweet( long author_,String text_){

        author=author_;
        text=text_;
    }

    public Tweet( long author_,String text_,long _visit){

        author=author_;
        text=text_;
        visit=_visit;
    }
}
