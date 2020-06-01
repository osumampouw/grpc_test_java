package benchmark.grpc.test.grpc_test_java;

public class ExampleRestResponse {
  private final String status;
  private final String result;
  private final String errorMessage;

  public ExampleRestResponse(String status, String result, String errorMessage) {
    this.status = status;
    this.result = result;
    this.errorMessage = errorMessage;
  }

  public String getStatus() {
    return status;
  }

  public String getResult() {
    return result;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
