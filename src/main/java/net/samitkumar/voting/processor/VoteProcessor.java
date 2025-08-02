package net.samitkumar.voting.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.samitkumar.voting.db.ErrorVote;
import net.samitkumar.voting.db.ErrorVoteRepository;
import net.samitkumar.voting.db.Vote;
import net.samitkumar.voting.db.VoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VoteProcessor {
    final ReactiveRedisTemplate<String, Vote> reactiveRedisTemplate;
    final VoteRepository voteRepository;
    final ErrorVoteRepository errorVoteRepository;

    @Value("${spring.application.data.redis.channel.name}")
    private String redisChannelName;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        reactiveRedisTemplate
                .listenToChannel(redisChannelName)
                .doOnNext(message -> log.info("[*] Received Message: {}", message))
                .map(ReactiveSubscription.Message::getMessage)
                .publishOn(Schedulers.boundedElastic())
                .map(vote -> {
                    log.info("Persisting Vote:: {}", vote);
                    return voteRepository.save(vote);
                })
                //stop acknowledging the message if there is an error during processing
                .onErrorContinue((throwable, o) -> {
                    log.error("Persisting Vote::onError {}", o, throwable);
                    var vote = (Vote) o;
                    errorVoteRepository.save(
                            new ErrorVote(null, vote.id(), vote.candidateId(), vote.voterId(), throwable.getMessage())
                    );
                })
                .doOnNext(db -> log.info("Persisting Vote::dbReply {}", db))
                .subscribe();
    }
}
