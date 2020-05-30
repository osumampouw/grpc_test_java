package benchmark.grpc.test.grpc_test_java;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(80)
                .addService(new ExampleProtoServiceImpl()).build();

        server.start();
        server.awaitTermination();
    }
}
