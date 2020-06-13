package rest.twitter.domian;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "twitterUser")
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

    private String password;

    private String email;

    private String phoneNumber;

    private String address;

    private String job;

    public User(){};

    public User( String name_,String profile_){
        name=name_;
        profile=profile_;
    }

    public User( String name_,String profile_,String _password){
        name=name_;
        profile=profile_;
        password=_password;
    }

    public User( String name_,String _email, String _phoneNumber, String _job,String _password){
        name=name_;
        email=_email;
        phoneNumber=_phoneNumber;
        job=_job;
        password=_password;
    }
}
