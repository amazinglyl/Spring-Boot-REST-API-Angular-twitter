package rest.twitter.domian;

import java.io.Serializable;

public class FollowTableId implements Serializable {
    long followedId;
    long followerId;

    public FollowTableId(){}

    public FollowTableId(long followedId_,long followerId_){
        followedId=followedId_;
        followerId=followerId_;
    }
}
