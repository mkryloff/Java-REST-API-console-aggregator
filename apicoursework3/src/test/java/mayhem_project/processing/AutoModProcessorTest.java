package mayhem_project.processing;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class AutoModProcessorTest
{

    @Test
    void autoRun_StartsAndStopsExecutor()
    {
        String simulatedInput = "\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        String[] args = {"AUTO", "weather,", "astronomy", "json", "2", "5"};
        assertDoesNotThrow(() -> AutoModProcessor.autoRun(args));

        System.setIn(System.in);
    }


}
