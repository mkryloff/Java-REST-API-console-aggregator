package mayhem_project.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mayhem_project.ApiDataStorage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonFilePrinter
{
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);

    public static void printFile(String filename, String sourceFilter) {
        try
        {
            List<ApiDataStorage> allData = mapper.readValue(new File(filename), new TypeReference<>() {});
            if (sourceFilter != null && !sourceFilter.isEmpty())
            {
                allData = allData.stream()
                        .filter(record -> record.source().equalsIgnoreCase(sourceFilter))
                        .toList();
            }
            for (ApiDataStorage record : allData)
            {
                System.out.println("ID: " + record.id());
                System.out.println("Source: " + record.source());
                System.out.println("Timestamp: " + record.timestamp());
                System.out.println("Data:");
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(record.data()));
                System.out.println("---");
            }
        } catch (IOException e)
        {
            System.err.println("Cannot read JSON file!");
        }
    }
}
