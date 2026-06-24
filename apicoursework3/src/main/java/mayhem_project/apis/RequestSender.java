package mayhem_project.apis;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class RequestSender
{
    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String sendRequest(HttpGet request)
    {
        try
        {
            return httpClient.execute(request, response -> EntityUtils.toString(response.getEntity()));
        }
        catch (IOException e)
        {
            System.err.println("ERROR: getting HTTP response failure!");
            return null;
        }
    }
}
