package ru.otus.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.SequenceMember;

public class SequenceStreamObserver implements StreamObserver<SequenceMember> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceStreamObserver.class);

    private int lastValueFromService = 0;

    public int getLastValueFromService() {
        return lastValueFromService;
    }

    public void setLastValueFromService(int value) {
        this.lastValueFromService = value;
    }

    @Override
    public void onNext(SequenceMember sequenceMember) {
        int member = sequenceMember.getMember();
        lastValueFromService = member;
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
