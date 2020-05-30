package benchmark.grpc.test.grpc_test_java;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class GrpcClient {

    private static void testBlocking (int retries, int maxNumThread) throws InterruptedException {
        if (retries <= 0 || maxNumThread <= 0) {
            throw new IllegalArgumentException();
        }
        Semaphore counter = new Semaphore(0);
        ExecutorService pool = Executors.newFixedThreadPool(maxNumThread);

        Channel channel = ManagedChannelBuilder.forAddress("localhost",
                80).usePlaintext().build();

        ExampleProtoServiceGrpc.ExampleProtoServiceBlockingStub stub =
                ExampleProtoServiceGrpc.newBlockingStub(channel);

        ExampleProtobufRequest request = ExampleProtobufRequest.newBuilder().setId("123").setCode("bbb").build();
        for (int i = 0; i < retries; i++) {
            pool.submit(() -> {
                ExampleProtobufResponse result =
                        stub.getResponse(request);
                System.out.println("request " + result);
                counter.release();
            });
        }

        counter.acquire(retries);
    }

    private static void testNonBlocking (int retries, int maxNumThread) throws InterruptedException {
        if (retries <= 0 || maxNumThread <= 0) {
            throw new IllegalArgumentException();
        }
        Semaphore counter = new Semaphore(0);
        ExecutorService pool = Executors.newFixedThreadPool(maxNumThread);
        Channel channel = NettyChannelBuilder.forAddress("localhost", 80).usePlaintext().build();
        ExampleProtoServiceGrpc.ExampleProtoServiceFutureStub stub =
                ExampleProtoServiceGrpc.newFutureStub(channel);
        ExampleProtobufRequest request = ExampleProtobufRequest.newBuilder().setId("123").setCode("bbb").build();

        for (int i = 0; i < retries; i++) {
            ListenableFuture<ExampleProtobufResponse> result =
                    stub.getResponse(request);
            Futures.addCallback(
                    result,
                    new FutureCallback<ExampleProtobufResponse>() {
                        @Override
                        public void onSuccess(@NullableDecl ExampleProtobufResponse result) {
                            System.out.println("Result is " + result);
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
    }

    public static void main(String[] args) throws InterruptedException {
        testNonBlocking(10, 5);
    }
}
