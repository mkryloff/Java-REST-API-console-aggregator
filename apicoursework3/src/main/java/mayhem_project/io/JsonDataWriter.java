package mayhem_project.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mayhem_project.ApiDataStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonDataWriter
{
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);

    public static synchronized void saveDataToFile(List<ApiDataStorage> data, String filename, boolean isAppend)
    {
        if (data.isEmpty())
        {
            return;
        }

        File file = new File(filename);
        try
        {
            List<ApiDataStorage> allData = new ArrayList<>();
            if (isAppend && file.exists())
            {
                allData.addAll(mapper.readValue(file, new TypeReference<List<ApiDataStorage>>() {}));
            }
            allData.addAll(data);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, allData);
        }
        catch (IOException e)
        {
            System.err.println("ERROR: cannot write data into JSON file");
        }
    }

}
