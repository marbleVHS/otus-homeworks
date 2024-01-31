package ru.otus.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.SequenceParams;

public class GRPCClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var remoteSequenceService = RemoteSequenceServiceGrpc.newStub(channel);

        SequenceStreamObserver responseObserver = new SequenceStreamObserver();
        remoteSequenceService.getSequenceAsync(
                SequenceParams.newBuilder().setFrom(1).setTo(30).build(),
                responseObserver
        );

        int currentValue = 0;

        for (int i = 0; i <= 50; i++) {
            currentValue = currentValue + 1 + responseObserver.getLastValueFromService();
            responseObserver.setLastValueFromService(0);
            LOGGER.info("currentValue: {}", currentValue);
            Thread.sleep(1000L);
        }

    }


}
