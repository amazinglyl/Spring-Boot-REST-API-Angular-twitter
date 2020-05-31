package rest.twitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.twitter.repository.*;
import rest.twitter.domian.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.*;

@Configuration
@Slf4j
public class LoadDataBase {

    @Bean
    CommandLineRunner initializeDataBase(UserRepository repository,FollowTableRepository repositoryFollowTable,TweetRepository tweetRepository, LikeTableRepository likeTableRepository){
        return args -> {
            //add users
            String[] firstName=new String[]{"First1","First2","First3","First4","First5","First6","First7","First8","First9","First10"};
            String[] LasttName=new String[]{"Last1","Last2","Last3","Last4","Last5","Last6","Last7","Last8","Last9","Last10"};
            for(String first:firstName){
                for(String last:LasttName){
                    log.info("Preloading " + repository.save(new User(first+" "+last, "Hello! I am "+ first+" "+last+" !")));
                }
            }

            // add follow and tweets randomly
            Set<String> set = new HashSet<>();
            Random rand=new Random();
            for(int i=0;i<10000;i++){
                int followedId=rand.nextInt(100)+1;
                int followerId=rand.nextInt(100)+1;
                String temp=""+followedId+" "+followerId;
                if(!set.contains(temp)&&followedId!=followerId) {
                    log.info("PreLoading" + repositoryFollowTable.save(new FollowTable(followedId, followerId)));
                    set.add(temp);
                }
                tweetRepository.save(new Tweet(followedId,"tweet"+i));
            }

            // add like randomly
            Set<String> likeSet = new HashSet<>();
            for(int i=0;i<10000;i++){
                int author=rand.nextInt(100)+1;
                int tweet=rand.nextInt(10000)+1;
                String temp=""+author+" "+tweet;
                if(!likeSet.contains(temp)) {
                    log.info("PreLoading" + likeTableRepository.save(new LikeTable(author, tweet)));
                    likeSet.add(temp);
                }
            }
//            log.info("Preloading " + repository.save(new User("Bilbo Baggins", "burglar")));
//            log.info("Preloading " + repository.save(new User("Frodo Baggins", "thief")));
//            log.info("Preloading " + repository.save(new User("Dad Baggins", "Berserk")));
//            //log.info("Preloading" + repositoryFollowTable.save(new FollowTable(1,2)));
//            log.info("PreLoading" +repositoryFollowTable.save(new FollowTable(1,3)));
//            log.info("PreLoading" +repositoryFollowTable.save(new FollowTable(2,3)));
//            log.info("PreLoading" +repositoryFollowTable.save(new FollowTable(2,1)));
        };
    }

}

