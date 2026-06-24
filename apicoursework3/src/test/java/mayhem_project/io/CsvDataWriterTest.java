package mayhem_project.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mayhem_project.ApiDataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CsvDataWriterTest {

    @TempDir
    Path tempDir;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void saveDataToFile_NewFile_WritesCsvWithHeader() throws JsonProcessingException
    {
        File file = tempDir.resolve("test.csv").toFile();
        JsonNode dataNode = mapper.readTree("{\"temp\":20.5,\"hum\":80}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        CsvDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        assertTrue(file.exists());
        List<ApiDataStorage> loaded = CsvDataWriter.readAll(file.getAbsolutePath());
        assertEquals(1, loaded.size());
        assertEquals("weatherApi", loaded.get(0).source());
        assertEquals("20.5", loaded.get(0).data().get("temp").asText());
    }

    @Test
    void saveDataToFile_AppendToExisting_AddsRowAndUpdatesHeader() throws Exception {
        File file = tempDir.resolve("test.csv").toFile();
        JsonNode data1 = mapper.readTree("{\"temp\":20}");
        JsonNode data2 = mapper.readTree("{\"hum\":70}");
        ApiDataStorage record1 = new ApiDataStorage("weatherApi", data1);
        ApiDataStorage record2 = new ApiDataStorage("weatherApi", data2);

        CsvDataWriter.saveDataToFile(List.of(record1), file.getAbsolutePath(), false);
        CsvDataWriter.saveDataToFile(List.of(record2), file.getAbsolutePath(), true);

        List<ApiDataStorage> loaded = CsvDataWriter.readAll(file.getAbsolutePath());
        assertEquals(2, loaded.size());
        assertEquals("20", loaded.get(0).data().get("temp").asText());
        assertEquals("70", loaded.get(1).data().get("hum").asText());
    }

    @Test
    void saveDataToFile_EmptyList_DoesNothing()
    {
        File file = tempDir.resolve("test.csv").toFile();
        CsvDataWriter.saveDataToFile(List.of(), file.getAbsolutePath(), false);
        assertFalse(file.exists());
    }
}
