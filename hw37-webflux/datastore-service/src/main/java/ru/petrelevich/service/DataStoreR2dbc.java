package ru.petrelevich.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.petrelevich.domain.Message;
import ru.petrelevich.repository.MessageRepository;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private static final String SPECIAL_ROOM_ID = "1408";

    private final MessageRepository messageRepository;
    private final Scheduler workerPool;

    public DataStoreR2dbc(Scheduler workerPool, MessageRepository messageRepository) {
        this.workerPool = workerPool;
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<Message> saveMessage(Message message) {
        log.info("saveMessage:{}", message);
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);
        if (roomId.equals(SPECIAL_ROOM_ID)) {
            return messageRepository.findAllMessages().delayElements(Duration.of(500, MILLIS), workerPool);
        }
        return messageRepository.findByRoomId(roomId).delayElements(Duration.of(500, MILLIS), workerPool);
    }

}
