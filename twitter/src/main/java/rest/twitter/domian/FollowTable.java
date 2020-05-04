package rest.twitter.domian;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@IdClass(FollowTableId.class)
public class FollowTable {


    @Id
    private long followedId;

    @Id
    private long followerId;

    public FollowTable(){};

    public FollowTable(long followedId_, long followerId_){
        followedId=followedId_;
        followerId=followerId_;
    }


}


