package rest.twitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.twitter.repository.*;

@Configuration
@Slf4j
public class LoadDataBase {

    @Bean
    CommandLineRunner initializeDataBase(UserRepository repository,FollowTableRepository repositoryFollowTable){
        return args -> {
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

