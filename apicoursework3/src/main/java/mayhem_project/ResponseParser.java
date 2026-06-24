package mayhem_project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseParser
{
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ApiDataStorage parseResponseToObject(String source, String response)
    {
        if (source == null || response == null || source.isEmpty() || response.isEmpty())
        {
            System.err.println("ERROR: unsuccessful json parsing of source: " + source);
            return null;
        }
        try
        {
            JsonNode jsonData = mapper.readTree(response);
            return new ApiDataStorage(source, jsonData);
        }
        catch (JsonProcessingException e)
        {
            System.err.println("ERROR: unsuccessful json parsing of source: " + source);
            return null;
        }
    }
}
