package net.samitkumar.voting.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.samitkumar.voting.db.VoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class VotingResultConfiguration {
    final VoteRepository voteRepository;
    final ObjectMapper objectMapper;

    @Bean
    Sinks.Many<String> sinks() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/results", session -> session
                .send(sinks().asFlux().map(session::textMessage))
                //echo back , if there is a message
                .and(session
                        .receive()
                        .map(webSocketMessage -> sinks().tryEmitNext(webSocketMessage.getPayloadAsText()))
                )
                .then());
        int order = -1;
        return new SimpleUrlHandlerMapping(map, order);
    }

    @Scheduled(fixedRate = 20000)
    void scheduled() {
        log.info("Scheduled trigger to notify result to all ws session");
        sinks().tryEmitNext(emitResponse());
    }

    @SneakyThrows
    private String emitResponse() {
        var votingResult = new VotingResults(
                LocalDateTime.now(),voteRepository.votingResults());
        return objectMapper
                .writeValueAsString(votingResult);
    }
}
