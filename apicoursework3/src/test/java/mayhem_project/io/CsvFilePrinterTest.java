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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CsvFilePrinterTest
{

    @TempDir
    Path tempDir;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void printFile_WithValidFile_DoesNotThrow() throws JsonProcessingException
    {
        File file = tempDir.resolve("test.csv").toFile();
        JsonNode dataNode = mapper.readTree("{\"temp\":20.5}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        CsvDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        assertDoesNotThrow(() -> CsvFilePrinter.printFile(file.getAbsolutePath(), null));
    }

    @Test
    void printFile_WithSourceFilter_DoesNotThrow() throws JsonProcessingException
    {
        File file = tempDir.resolve("test.csv").toFile();
        JsonNode dataNode = mapper.readTree("{\"temp\":20.5}");
        ApiDataStorage record = new ApiDataStorage("weatherApi", dataNode);
        CsvDataWriter.saveDataToFile(List.of(record), file.getAbsolutePath(), false);

        assertDoesNotThrow(() -> CsvFilePrinter.printFile(file.getAbsolutePath(), "weatherApi"));
    }

    @Test
    void printFile_WithNonExistentFile_DoesNotThrow()
    {
        assertDoesNotThrow(() -> CsvFilePrinter.printFile("nonexistent.csv", null));
    }

    @Test
    void printFile_WithEmptyFile_DoesNotThrow() throws Exception
    {
        File file = tempDir.resolve("empty.csv").toFile();
        file.createNewFile();
        assertDoesNotThrow(() -> CsvFilePrinter.printFile(file.getAbsolutePath(), null));
    }
}