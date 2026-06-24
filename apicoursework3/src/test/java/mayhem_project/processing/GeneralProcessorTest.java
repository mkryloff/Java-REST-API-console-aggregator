package mayhem_project.processing;

import mayhem_project.apis.ApiClient;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class  GeneralProcessorTest {

    @Test
    void parseApiPreferences_ValidNumbers_ReturnsCorrectNames()
    {
        String[] result = GeneralProcessor.parseApiPreferences("1,2");
        assertArrayEquals(new String[]{"weather", "astronomy"}, result);
    }

    @Test
    void parseApiPreferences_WithSpaces_ReturnsCorrectNames()
    {
        String[] result = GeneralProcessor.parseApiPreferences("1, 2 ,3");
        assertArrayEquals(new String[]{"weather", "astronomy", "stockMarket"}, result);
    }

    @Test
    void parseApiPreferences_InvalidToken_IgnoresAndSkips()
    {
        String[] result = GeneralProcessor.parseApiPreferences("1,4,2");
        assertArrayEquals(new String[]{"weather", "astronomy"}, result);
    }

    @Test
    void collectApiClients_DuplicateNames_ReturnsUniqueClients()
    {
        String[] apis = {"weather", "weather", "astronomy", "astronomy"};
        Map<String, ApiClient> clients = GeneralProcessor.collectApiClients(apis);
        assertEquals(2, clients.size());
        assertTrue(clients.containsKey("weatherApi"));
        assertTrue(clients.containsKey("astronomyApi"));
    }

    @Test
    void collectApiClients_ValidNames_ReturnsCorrectMapping()
    {
        String[] apis = {"weather", "stockMarket"};
        Map<String, ApiClient> clients = GeneralProcessor.collectApiClients(apis);
        assertNotNull(clients.get("weatherApi"));
        assertNotNull(clients.get("stockMarketApi"));
    }

    @Test
    void collectApiClients_InvalidName_PrintsErrorButIgnores()
    {
        String[] apis = {"weather", "Bebebebebe"};
        Map<String, ApiClient> clients = GeneralProcessor.collectApiClients(apis);
        assertEquals(1, clients.size());
        assertTrue(clients.containsKey("weatherApi"));
    }
}