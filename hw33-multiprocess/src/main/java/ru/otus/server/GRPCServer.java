package ru.otus.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.server.service.RemoteSequenceServiceImpl;
import ru.otus.server.service.SequenceGeneratorService;
import ru.otus.server.service.SequenceGeneratorServiceImpl;

import java.io.IOException;

public class GRPCServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        SequenceGeneratorService sequenceGeneratorService = new SequenceGeneratorServiceImpl();
        RemoteSequenceServiceImpl remoteSequenceService = new RemoteSequenceServiceImpl(sequenceGeneratorService);

        var server =
                ServerBuilder.forPort(SERVER_PORT).addService(remoteSequenceService).build();
        server.start();
        LOGGER.info("server waiting for client connections...");
        server.awaitTermination();

    }

}
