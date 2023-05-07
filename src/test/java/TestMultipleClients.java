import client.CreateWhiteboard;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.Server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMultipleClients {

    private Server server;
    private ExecutorService pool;

    @BeforeEach
    void setUp() throws IOException {
        pool = Executors.newFixedThreadPool(10);
        pool.submit(()->{
            server = new Server();
        });
    }

    @AfterEach
    void tearDown() throws IOException {
        server = null;
        pool.shutdownNow();
    }

    @Test
    public void testServer() throws IOException, InterruptedException {
        for (int i=0; i<10;i++){
            CreateWhiteboard createWhiteboard = new CreateWhiteboard(1099,"localhost",("August"+i));
            createWhiteboard.createWhiteboardApp();
        }
        Thread.sleep(1000);
    }

}
