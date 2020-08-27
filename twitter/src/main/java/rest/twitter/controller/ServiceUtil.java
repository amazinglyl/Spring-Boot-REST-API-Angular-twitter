package rest.twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.twitter.domian.LikeTable;
import rest.twitter.repository.LikeTableRepository;
import rest.twitter.repository.TweetRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceUtil {
    @Autowired
    LikeTableRepository likeTableRepository;

    @Autowired
    TweetRepository tweetRepository;

    public Set<Long> likeList(long id){
        List<LikeTable> list=likeTableRepository.findByAuthor(id);
        Set<Long> set=new HashSet<>();
        for(LikeTable table: list){
            set.add(table.getTweet());
        }
        return set;
    }
}
