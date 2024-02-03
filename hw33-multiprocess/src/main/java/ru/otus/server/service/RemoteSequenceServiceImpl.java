package ru.otus.server.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.SequenceMember;
import ru.otus.protobuf.SequenceParams;

import java.util.List;

public class RemoteSequenceServiceImpl extends RemoteSequenceServiceGrpc.RemoteSequenceServiceImplBase {

    private final SequenceGeneratorService sequenceGeneratorService;

    public RemoteSequenceServiceImpl(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public void getSequenceAsync(SequenceParams request, StreamObserver<SequenceMember> responseObserver) {
        List<Integer> sequenceMembers = sequenceGeneratorService.getSequenceMembers(request.getFrom(), request.getTo());
        sequenceMembers.forEach(member -> {
            responseObserver.onNext(SequenceMember.newBuilder().setMember(member).build());
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                responseObserver.onError(e);
                Thread.currentThread().interrupt();
            }
        });
        responseObserver.onCompleted();
    }
}
