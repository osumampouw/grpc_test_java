package benchmark.grpc.test.grpc_test_java;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ExampleRestController {
  Random random = new Random();

  @GetMapping("/")
  public ExampleRestResponse getResponse(ExampleRestRequest request) {
    if (request.getId() != null && request.getCode() != null) {
      if (random.nextBoolean()) {
        return new ExampleRestResponse("200", "type 1 result", null);
      } else {
        return new ExampleRestResponse("200", "type 2 result", null);
      }
    } else {
      return new ExampleRestResponse(
          "500", null, "you send a request where the id or code is undefined");
    }
  }
}
