package net.samitkumar.voting;

import net.samitkumar.voting.db.Vote;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class VotingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingApplication.class, args);
	}

	@Bean
	public ReactiveRedisTemplate<String, Vote> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

		Jackson2JsonRedisSerializer<Vote> serializer = new Jackson2JsonRedisSerializer<>(Vote.class);
		RedisSerializationContext.RedisSerializationContextBuilder<String, Vote> builder =
				RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
		RedisSerializationContext<String, Vote> context = builder.value(serializer).build();
		return new ReactiveRedisTemplate<>(factory, context);

		/*
		//OR
		RedisSerializationContext<String, Vote> serializationContext = RedisSerializationContext
				.<String, Vote>newSerializationContext(new StringRedisSerializer())
				.key(new StringRedisSerializer())
				.value(new Jackson2JsonRedisSerializer<>(Vote.class))
				.hashKey(new Jackson2JsonRedisSerializer<>(String.class))
				.hashValue(new Jackson2JsonRedisSerializer<>(Vote.class))
				.build();

		return new ReactiveRedisTemplate<>(factory, serializationContext);*/

	}

}
