package benchmark.grpc.test.grpc_test_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ExampleRestServer {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ExampleRestServer.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", "80"));
    app.run(args);
  }
}
