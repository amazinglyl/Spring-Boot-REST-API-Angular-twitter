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
            String[] name=new String[]{"William","Jason","Eric","Liam"};
            for(String first:name){
                    log.info("Preloading " + repository.save(new User(first, "Hello! I am "+ first+" !","123456")));
            }

            String[] tweets=new String[]{"You have no idea how much my heart races when I see you.",
                    "Since the time I've met you, I cry a little less, laugh a little harder and smile all the more, just because I have you, my life is a better place.",
                    "Every day with you is a wonderful addition to my life's journey.",
                    "Just when I think that it is impossible to love you any more than I already do, you prove me wrong.",
                    "I’m having one of those days that make me realize how lost I’d be without you.",
                    "Your cute smile can melt even the icy heart, which I have had before I met you. Thanks for all the happiness that you gave me, I love you.",
                    "I will always have this piece of my heart that smiles whenever I think about you.",
                    "My heart for you will never break. My smile for you will never fade. My love for you will never end. I love you!",
                    "Only one single phrase makes my heart beat faster – it is your name and the word forever.",
                    "When you need someone to be there for you, I’ll be right there by your side always!",
                    "Sometimes I wonder if love is worth fighting for. Then I look at you. I’m ready for war.",
                    "I want to be your favorite hello, and hardest goodbye.",
                    "Explaining to you how much and why I love you, would be like me describing how water tastes. It’s impossible.",
                    " I only saw you for a second, but it made my day.",
                    "I miss my sleep in the night and I miss the light in my day, It’s a wonderful feel to melt in love and I melt at your love!",
                    "Every love story is beautiful … but ours is my favorite!"};
            // add follow and tweets randomly
            Set<String> set = new HashSet<>();
            Random rand=new Random();
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++) {
                    String temp = "" + i + " " + j;
                    if (!set.contains(temp) && i != j) {
                        log.info("PreLoading" + repositoryFollowTable.save(new FollowTable(i+1, j+1)));
                        set.add(temp);
                    }

                    log.info("preLoading" + tweetRepository.save(new Tweet(i+1, tweets[4*i+j],name[i])));
                }
            }

            // add like randomly
            Set<String> likeSet = new HashSet<>();
            for(int i=0;i<50;i++){
                int author=rand.nextInt(4)+1;
                int tweet=rand.nextInt(16)+1;
                String temp=""+author+" "+tweet;
                if(!likeSet.contains(temp)) {
                    log.info("PreLoading" + likeTableRepository.save(new LikeTable(author, tweet)));
                    likeSet.add(temp);
                }
            }
        };
    }

}

