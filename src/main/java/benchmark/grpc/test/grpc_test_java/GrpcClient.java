package benchmark.grpc.test.grpc_test_java;

import ch.qos.logback.classic.Level;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class GrpcClient {

  private static void testBlocking(int retries, ExecutorService pool, Channel channel)
      throws InterruptedException {
    if (retries <= 0) {
      throw new IllegalArgumentException();
    }
    Semaphore counter = new Semaphore(0);
    ExampleProtoServiceGrpc.ExampleProtoServiceBlockingStub stub =
        ExampleProtoServiceGrpc.newBlockingStub(channel);

    ExampleProtobufRequest request =
        ExampleProtobufRequest.newBuilder().setId("123").setCode("bbb").build();

    final long start = System.nanoTime();

    for (int i = 0; i < retries; i++) {
      pool.submit(
          () -> {
            ExampleProtobufResponse result = stub.getResponse(request);
            counter.release();
          });
    }

    counter.acquire(retries);
    final long finish = System.nanoTime();
    final long durationMs = TimeUnit.NANOSECONDS.toMillis(finish - start);
    System.out.println("ran for " + durationMs + " ms");
  }

  private static void testNonBlocking(int retries, ExecutorService pool, Channel channel)
      throws InterruptedException {
    if (retries <= 0) {
      throw new IllegalArgumentException();
    }
    Semaphore counter = new Semaphore(0);

    ExampleProtoServiceGrpc.ExampleProtoServiceFutureStub stub =
        ExampleProtoServiceGrpc.newFutureStub(channel);
    ExampleProtobufRequest request =
        ExampleProtobufRequest.newBuilder().setId("123").setCode("bbb").build();
    final long start = System.nanoTime();
    for (int i = 0; i < retries; i++) {
      ListenableFuture<ExampleProtobufResponse> result = stub.getResponse(request);
      Futures.addCallback(
          result,
          new FutureCallback<ExampleProtobufResponse>() {
            @Override
            public void onSuccess(@NullableDecl ExampleProtobufResponse result) {
              counter.release();
            }

            @Override
            public void onFailure(Throwable t) {
              System.out.println("error occurred " + t);
              counter.release();
            }
          },
          pool);
    }
    counter.acquire(retries);
    final long finish = System.nanoTime();
    final long durationMs = TimeUnit.NANOSECONDS.toMillis(finish - start);
    System.out.println("ran for " + durationMs + " ms");
  }

  public static void main(String[] args) throws InterruptedException {
    // workaround to set log level to info because somehow this application magically inject a slf4j
    // logback
    // and the default log level is DEBUG and netty is going to flood the logs
    ch.qos.logback.classic.Logger rootLogger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    rootLogger.setLevel(Level.INFO);

    int maxNumThread = 100;
    ExecutorService pool = Executors.newFixedThreadPool(maxNumThread);

    Channel nonBlockingChannel =
        NettyChannelBuilder.forAddress("localhost", 80).usePlaintext().build();
    System.out.println("Test warm up non blocking");

    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(5000, pool, nonBlockingChannel);
    testNonBlocking(500, pool, nonBlockingChannel);
    testNonBlocking(5000, pool, nonBlockingChannel);
    testNonBlocking(500, pool, nonBlockingChannel);
    testNonBlocking(5000, pool, nonBlockingChannel);

    System.out.println("Test starts now non blocking");
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);
    testNonBlocking(10000, pool, nonBlockingChannel);

    Channel blockingChannel =
        ManagedChannelBuilder.forAddress("localhost", 80).usePlaintext().build();
    System.out.println("Test warm up blocking");
    testBlocking(50, pool, blockingChannel);
    testBlocking(50, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(5000, pool, blockingChannel);
    testBlocking(5000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);

    System.out.println("Test starts now blocking");
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
    testBlocking(10000, pool, blockingChannel);
  }
}
