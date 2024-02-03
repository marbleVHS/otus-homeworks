package ru.otus.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.SequenceMember;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceStreamObserver implements StreamObserver<SequenceMember> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceStreamObserver.class);

    private final AtomicInteger lastValueFromService = new AtomicInteger(0);

    public int getAndSetLastValueFromService(int newValue) {
        return lastValueFromService.getAndSet(newValue);
    }

    @Override
    public void onNext(SequenceMember sequenceMember) {
        int member = sequenceMember.getMember();
        lastValueFromService.set(member);
        LOGGER.info("new value: {}", member);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error("Error during observing sequence stream", throwable);
    }

    @Override
    public void onCompleted() {
        LOGGER.info("Request completed");
    }

}
