package mayhem_project.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mayhem_project.ApiDataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JsonDataWriterTest
{

    @TempDir
    Path tempDir;

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    void saveDataToFile_NewFile_WritesData() throws IOException
    {
        File file = tempDir.resolve("test.json").toFile();
        JsonNode dataNode = mapper.readTree("{\"key\":\"value\"}");
        ApiDataStorage record = new ApiDataStorage("testSource", dataNode);
        JsonDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        List<ApiDataStorage> loaded = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, ApiDataStorage.class));
        assertEquals(1, loaded.size());
        assertEquals("testSource", loaded.get(0).source());
        assertEquals("value", loaded.get(0).data().get("key").asText());
    }

    @Test
    void saveDataToFile_AppendToExisting_AddsData() throws IOException
    {
        File file = tempDir.resolve("test.json").toFile();
        JsonNode data1 = mapper.readTree("{\"a\":1}");
        JsonNode data2 = mapper.readTree("{\"b\":2}");
        ApiDataStorage record1 = new ApiDataStorage("src1", data1);
        ApiDataStorage record2 = new ApiDataStorage("src2", data2);

        JsonDataWriter.saveDataToFile(List.of(record1), file.getAbsolutePath(), false);
        JsonDataWriter.saveDataToFile(List.of(record2), file.getAbsolutePath(), true);

        List<ApiDataStorage> loaded = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, ApiDataStorage.class));
        assertEquals(2, loaded.size());
        assertEquals("src1", loaded.get(0).source());
        assertEquals("src2", loaded.get(1).source());
    }

    @Test
    void saveDataToFile_EmptyList_DoesNothing()
    {
        File file = tempDir.resolve("test.json").toFile();
        JsonDataWriter.saveDataToFile(List.of(), file.getAbsolutePath(), false);
        assertFalse(file.exists());
    }
}