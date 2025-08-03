package net.samitkumar.voting.vote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.samitkumar.voting.db.CandidateRepository;
import net.samitkumar.voting.db.Vote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class VoteRouter {

    static final String VOTE_COOKIE_NAME = "voteId";
    final ReactiveRedisTemplate<String, Vote> reactiveRedisTemplate;
    final CandidateRepository candidateRepository;

    @Value("${spring.data.redis.channel-name}")
    private String redisChannelName;

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route()
                .GET("/candidate", this::allCandidates)
                .GET("/candidate/{id}", this::candidateById)
                .POST("/vote", this::handleVote)
                .after((req,res) -> {
                    log.info("{} {} {}", req.method(), req.path(), res.statusCode());
                    return res;
                })
                .build();
    }

    private Mono<ServerResponse> candidateById(ServerRequest request) {
        return Mono.fromCallable(() -> candidateRepository.findById(Long.parseLong(request.pathVariable("id"))).orElse(null))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(candidate -> ServerResponse.ok().bodyValue(candidate))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> allCandidates(ServerRequest request) {
        return Mono.fromCallable(candidateRepository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private Mono<ServerResponse> handleVote(ServerRequest request) {
        return Mono.fromCallable(() -> request.cookies().getFirst(VOTE_COOKIE_NAME))
                .flatMap(ServerResponse.badRequest()::bodyValue)
                .switchIfEmpty(request
                        .bodyToMono(Vote.class)
                        .doOnNext(vote -> log.info("Received vote: {}", vote))
                        .map(vote -> new Vote(null, vote.candidateId(), UUID.randomUUID().toString(), LocalDateTime.now()))
                        .flatMap(v -> reactiveRedisTemplate.convertAndSend(redisChannelName, v)
                                .doOnNext(reply -> log.info("Received vote send to redis: {}", reply))
                                .map(redisReplyId -> Map.of("voterId", v.voterId(), "processorId", redisReplyId))
                                .flatMap(result -> ServerResponse
                                        .ok()
                                        .cookie(ResponseCookie.from(VOTE_COOKIE_NAME, v.voterId()).build())
                                        .bodyValue(result)
                                )
                        )
                );
    }

}

