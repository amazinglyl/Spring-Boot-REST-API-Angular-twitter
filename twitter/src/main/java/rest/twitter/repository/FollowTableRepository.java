package rest.twitter.repository;

import rest.twitter.domian.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowTableRepository extends JpaRepository<FollowTable,FollowTableId> {
    List<FollowTable> findByFollowedId(long followedId);
    List<FollowTable> findByFollowerId(long followerId);
    long countByFollowedIdAndFollowerId(long followedId, long followerId);
}
