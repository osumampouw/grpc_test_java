package benchmark.grpc.test.grpc_test_java;

import io.grpc.stub.StreamObserver;

public class ExampleProtoServiceImpl extends ExampleProtoServiceGrpc.ExampleProtoServiceImplBase{

    @Override
    public void getResponse (ExampleProtobufRequest request, StreamObserver<ExampleProtobufResponse>responseObserver) {
        ExampleProtobufResponse response;

        if (request.getId() != null && request.getCode() != null) {
            response = ExampleProtobufResponse.newBuilder().setStatus("200")
                    .setResult("Foo Bar").build();
        } else {
            response = ExampleProtobufResponse.newBuilder().setStatus("500")
                    .setErrorMessage("Need Id and Code to be sent").build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
