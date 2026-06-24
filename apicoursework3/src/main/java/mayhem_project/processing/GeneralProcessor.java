package mayhem_project.processing;

import mayhem_project.apis.ApiClient;
import mayhem_project.apis.AstronomyApi;
import mayhem_project.apis.StockMarketApi;
import mayhem_project.apis.WeatherApi;

import java.util.*;

public class GeneralProcessor
{
    public static Map<String, ApiClient> collectApiClients(String[] apis)
    {
        Map<String, ApiClient> clients = new HashMap<>();
        for (String api:apis)
        {
            switch (api)
            {
                case "weather" -> clients.putIfAbsent("weatherApi", new WeatherApi());
                case "astronomy" -> clients.putIfAbsent("astronomyApi", new AstronomyApi());
                case "stockMarket" -> clients.putIfAbsent("stockMarketApi", new StockMarketApi());
                default -> System.err.println("Incorrect API name chosen: " + api);
            }
        }
        return clients;
    }


    public static String[] parseApiPreferences(String input)
    {
        String[] tokens = input.split("\\s*,\\s*");
        List<String> result = new ArrayList<>();
        for (String token : tokens)
        {
            switch (token)
            {
                case "1" -> result.add("weather");
                case "2" -> result.add("astronomy");
                case "3" -> result.add("stockMarket");
            }
        }
        return result.toArray(new String[0]);
    }
}
