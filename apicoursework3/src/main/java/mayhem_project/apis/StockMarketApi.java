package mayhem_project.apis;

import org.apache.hc.client5.http.classic.methods.HttpGet;

public class StockMarketApi implements ApiClient
{
    @Override
    public String getSourceName()
    {
        return "stockMarketApi";
    }

    @Override
    public String buildUrl()
    {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo";
    }

    @Override
    public String getStringResponse()
    {
        String url = buildUrl();
        HttpGet request = new HttpGet(url);
        return RequestSender.sendRequest(request);
    }
}
