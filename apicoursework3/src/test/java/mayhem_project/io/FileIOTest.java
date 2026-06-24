package mayhem_project.io;

import com.fasterxml.jackson.core.JsonProcessingException;
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

class FileIOTest
{
    @TempDir
    Path tempDir;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    void writeData_WithJsonExtension_WritesJsonFile() throws IOException
    {
        String filename = "result.json";
        File file = new File(filename);
        if (file.exists()) file.delete();

        JsonNode dataNode = mapper.readTree("{\"test\":123}");
        ApiDataStorage record = new ApiDataStorage("testSource", dataNode);

        FileIO.writeData(List.of(record), "json", false);

        assertTrue(file.exists());
        List<ApiDataStorage> loaded = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, ApiDataStorage.class));
        assertEquals(1, loaded.size());
        assertEquals("testSource", loaded.get(0).source());

        file.delete();
    }

    @Test
    void writeData_WithCsvExtension_WritesCsvFile() throws Exception {
        File file = new File("result.csv");
        file.delete();

        JsonNode dataNode = mapper.readTree("{\"temp\":20.5}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);

        FileIO.writeData(List.of(record), "csv", false);

        assertTrue(file.exists());
        List<ApiDataStorage> loaded = CsvDataWriter.readAll(file.getAbsolutePath());
        assertEquals(1, loaded.size());
        assertEquals("weatherApi", loaded.get(0).source());
        assertEquals("20.5", loaded.get(0).data().get("temp").asText());

        file.delete();
    }

    @Test
    void writeData_WithUnknownExtension_PrintsErrorAndDoesNothing()
    {
        assertDoesNotThrow(() -> FileIO.writeData(List.of(), "xml", false));
    }

    @Test
    void writeData_WithEmptyData_DoesNothing()
    {
        File jsonFile = tempDir.resolve("result.json").toFile();
        File csvFile = tempDir.resolve("result.csv").toFile();

        FileIO.writeData(List.of(), "json", false);
        FileIO.writeData(List.of(), "csv", false);

        assertFalse(jsonFile.exists());
        assertFalse(csvFile.exists());
    }

    @Test
    void printData_WithJsonExtension_DoesNotThrow() throws JsonProcessingException
    {
        File file = tempDir.resolve("result.json").toFile();
        JsonNode dataNode = mapper.readTree("{\"test\":123}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        JsonDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        String[] filters = {"1"};
        assertDoesNotThrow(() -> FileIO.printData("json", filters));
    }

    @Test
    void printData_WithCsvExtension_DoesNotThrow() throws JsonProcessingException
    {
        File file = tempDir.resolve("result.csv").toFile();
        JsonNode dataNode = mapper.readTree("{\"temp\":20.5}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        CsvDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        String[] filters = {"1"};
        assertDoesNotThrow(() -> FileIO.printData("csv", filters));
    }

    @Test
    void printData_WithUnknownExtension_PrintsErrorAndDoesNothing()
    {
        String[] filters = {"1"};
        assertDoesNotThrow(() -> FileIO.printData("xml", filters));
    }

    @Test
    void printData_WithAllFilterA_DoesNotThrow() throws JsonProcessingException
    {
        File file = tempDir.resolve("result.json").toFile();
        JsonNode dataNode = mapper.readTree("{\"test\":123}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        JsonDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        String[] filters = {"A"};
        assertDoesNotThrow(() -> FileIO.printData("json", filters));
    }

    @Test
    void printData_WithInvalidFilter_DoesNotThrow() throws JsonProcessingException
    {
        File file = tempDir.resolve("result.json").toFile();
        JsonNode dataNode = mapper.readTree("{\"test\":123}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        JsonDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        String[] filters = {"99"};
        assertDoesNotThrow(() -> FileIO.printData("json", filters));
    }

    @Test
    void printData_WithMultipleFilters_DoesNotThrow() throws Exception
    {
        File file = tempDir.resolve("result.json").toFile();
        JsonNode dataNode = mapper.readTree("{\"test\":123}");
        ApiDataStorage record1 = new ApiDataStorage("weatherApi", dataNode);
        ApiDataStorage record2 = new ApiDataStorage("astronomyApi", dataNode);
        JsonDataWriter.saveDataToFile(List.of(record1, record2), file.getAbsolutePath(), false);

        String[] filters = {"1", "2"};
        assertDoesNotThrow(() -> FileIO.printData("json", filters));
    }
}