package rest.twitter.domian;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="profile")
    private String profile;

    @Column(name="tweets")
    private long tweets;

    @Column(name="followers")
    private long followers;

    private  long followings;

    public User(){};

    public User( String name_,String profile_){
        name=name_;
        profile=profile_;
    }


}
