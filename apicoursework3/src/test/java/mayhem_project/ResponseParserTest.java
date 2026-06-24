package mayhem_project;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseParserTest {

    @Test
    void parseResponseToObject_ValidJson_ReturnsApiDataStorage()
    {
        String validJson = "{\"name\":\"test\",\"value\":123}";
        ApiDataStorage result = ResponseParser.parseResponseToObject("testSource", validJson);
        assertNotNull(result);
        assertEquals("testSource", result.source());
        JsonNode data = result.data();
        assertEquals("test", data.get("name").asText());
        assertEquals(123, data.get("value").asInt());
    }

    @Test
    void parseResponseToObject_InvalidJson_ReturnsNull()
    {
        String invalidJson = "this is not json";
        ApiDataStorage result = ResponseParser.parseResponseToObject("testSource", invalidJson);
        assertNull(result);
    }

    @Test
    void parseResponseToObject_NullResponse_ReturnsNull()
    {
        ApiDataStorage result = ResponseParser.parseResponseToObject("testSource", null);
        assertNull(result);
    }

    @Test
    void parseResponseToObject_EmptyString_ReturnsNull()
    {
        ApiDataStorage result = ResponseParser.parseResponseToObject("testSource", "");
        assertNull(result);
    }
}