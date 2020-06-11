package rest.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import rest.twitter.domian.Tweet;


@Configuration
public class redisConfig {

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory(){
//        return new JedisConnectionFactory();
//    }

//    @Bean
//    RedisConnectionFactory redisConnectionFactory(){
//        return new JedisConnectionFactory();
//    }


    @Bean(name="template1")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer redisSerializer=new StringRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(redisSerializer);
//        redisTemplate.setHashKeySerializer(redisSerializer);
//        redisTemplate.setHashValueSerializer(redisSerializer);
        //redisTemplate.setDefaultSerializer(redisSerializer);

        return  redisTemplate;
    }

    @Bean(name = "template2")
    public RedisTemplate<String, String> redisTemplateForMap(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer redisSerializer=new StringRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);

        return redisTemplate;
    }

}
