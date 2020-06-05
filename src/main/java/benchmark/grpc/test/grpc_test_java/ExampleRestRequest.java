package benchmark.grpc.test.grpc_test_java;

public class ExampleRestRequest {
  String id;
  String code;

  public ExampleRestRequest(String id, String code) {
    this.id = id;
    this.code = code;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
