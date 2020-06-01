package benchmark.grpc.test.grpc_test_java;

import io.grpc.stub.StreamObserver;

import java.util.Random;

public class ExampleProtoServiceImpl extends ExampleProtoServiceGrpc.ExampleProtoServiceImplBase {

  Random random = new Random();

  @Override
  public void getResponse(
      ExampleProtobufRequest request, StreamObserver<ExampleProtobufResponse> responseObserver) {
    ExampleProtobufResponse response;

    if (request.getId() != null && request.getCode() != null) {
      if (random.nextBoolean()) {
        response =
            ExampleProtobufResponse.newBuilder()
                .setStatus("200")
                .setResult("type 1 result")
                .build();
      } else {
        response =
            ExampleProtobufResponse.newBuilder()
                .setStatus("200")
                .setResult("type 2 result")
                .build();
      }
    } else {
      response =
          ExampleProtobufResponse.newBuilder()
              .setStatus("500")
              .setErrorMessage("you send a request where the id or code is undefined")
              .build();
    }

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
