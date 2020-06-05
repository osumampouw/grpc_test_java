package benchmark.grpc.test.grpc_test_java;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.Semaphore;

//TODO still Work in progress
public class ExampleRestClient {

    private static final String url = "http://localhost:80";
    private static final OkHttpClient client = new OkHttpClient();

    private static void runTest(int retries) throws InterruptedException, IOException {
        Semaphore counter = new Semaphore(0);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

//        WebClient client = WebClient.create(url);
//        Mono<ClientResponse> result = client.get()
//                .uri("/")
//                .accept(MediaType.TEXT_PLAIN)
//                .exchange();
//        System.out.println("result = " + result.flatMap(res -> res.bodyToMono(String.class)).block());
//        Mono<String> response = client.get().uri("/").retrieve().bodyToMono(String.class);
//        response.subscribe(new Subscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                // we don't do backpressure for now
//            }
//
//            @Override
//            public void onNext(String exampleRestResponse) {
//                System.out.println("received " + exampleRestResponse);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                System.out.println("error occurred " + t);
//            }
//
//            @Override
//            public void onComplete() {
//                counter.release();
//            }
//        });
//        System.out.println("foo");
//        counter.acquire(retries);
//        System.out.println("bar");
    }

    public static void main (String[] args) throws InterruptedException, IOException {
        // workaround to set log level to info because somehow this application magically inject a slf4j
        // logback
        // and the default log level is DEBUG and netty is going to flood the logs
        ch.qos.logback.classic.Logger rootLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.WARN);
        runTest(1);
        System.out.println("Client is done");
    }

}
