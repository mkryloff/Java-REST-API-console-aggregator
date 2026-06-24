package mayhem_project;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record ApiDataStorage(
        String id,
        String source,
        Instant timestamp,
        JsonNode data
)
{
    public ApiDataStorage(String source, JsonNode data)
    {
        this(UUID.randomUUID().toString(), source, Instant.now(), data);
    }
}
