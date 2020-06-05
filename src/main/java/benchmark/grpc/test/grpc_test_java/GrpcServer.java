package benchmark.grpc.test.grpc_test_java;

import ch.qos.logback.classic.Level;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GrpcServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    // workaround to set log level to warn because somehow grpc magically inject a slf4j
    // logback into netty and the default log level is DEBUG and netty is going to flood the logs
    ch.qos.logback.classic.Logger rootLogger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    rootLogger.setLevel(Level.WARN);

    Server server = ServerBuilder.forPort(80).addService(new ExampleProtoServiceImpl()).build();
    server.start();
    System.out.println("Server started");
    server.awaitTermination();
  }
}
