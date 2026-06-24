package mayhem_project.parallelProcessing;

import mayhem_project.apis.ApiClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.File;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParallelApiExecutorTest
{
    @Mock
    ApiClient client1;

    @Test
    void start_WhenAppendFalse()
    {
        String filename = "result.json";
        File file = new File(filename);
        try
        {
            assertTrue(file.createNewFile());

            Map<String, ApiClient> clients = Map.of("c1", client1);
            ParallelApiExecutor executor = new ParallelApiExecutor(clients, "json", 2, 1, false);
            executor.start();
            assertFalse(file.exists());
            executor.stop();
        }
        catch (Exception e)
        {
            file.delete();
        }
    }

    @Test
    void start_WhenAlreadyStarted()
    {
        Map<String, ApiClient> clients = Map.of("c1", client1);
        ParallelApiExecutor executor = new ParallelApiExecutor(clients, "json", 1, 1, true);
        executor.start();
        executor.start();
        assertTrue(executor.getIsStarted());
        executor.stop();
    }

    @Test
    void stop_WhenNotStarted()
    {
        Map<String, ApiClient> clients = Map.of("c1", client1);
        ParallelApiExecutor executor = new ParallelApiExecutor(clients, "json", 1, 1, true);
        executor.stop();
        assertFalse(executor.getIsStarted());
    }

    @Test
    void start_WhenInvalidTimeInterval()
    {
        Map<String, ApiClient> clients = Map.of("c1", client1);
        ParallelApiExecutor executor = new ParallelApiExecutor(clients, "json", 1, -20, true);
        assertDoesNotThrow(executor::start);
        assertTrue(executor.getIsStarted());
    }
}