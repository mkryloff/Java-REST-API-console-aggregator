package mayhem_project.apis;

import org.apache.hc.client5.http.classic.methods.HttpGet;

public class WeatherApi implements ApiClient
{
    @Override
    public String getSourceName()
    {
        return "weatherApi";
    }

    @Override
    public String buildUrl()
    {
        return "https://api.open-meteo.com/v1/forecast"
                + "?latitude=59.9745800&longitude=29.1943800"
                + "&timezone=auto&hourly=visibility,cloud_cover"
                + "&hourly=temperature_2m,pressure_msl,precipitation";
    }

    @Override
    public String getStringResponse()
    {
        String url = buildUrl();
        HttpGet request = new HttpGet(url);
        return RequestSender.sendRequest(request);
    }
}
