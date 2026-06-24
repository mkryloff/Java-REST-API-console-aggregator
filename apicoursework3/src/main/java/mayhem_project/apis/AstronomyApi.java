package mayhem_project.apis;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import java.util.Base64;

public class AstronomyApi implements ApiClient
{
    @Override
    public String getSourceName()
    {
        return "astronomyApi";
    }

    @Override
    public String buildUrl()
    {
        return "https://api.astronomyapi.com/api/v2/bodies/positions"
                + "?longitude=-84.39733&latitude=33.775867&elevation=1"
                + "&from_date=2026-03-10&to_date=2026-03-10&time=11%3A12%3A36";
    }

    @Override
    public String getStringResponse()
    {
        final String AppID = "9a7760bf-571d-4e17-b3f6-25f7c928eb7b";
        final String AppSecret = "30ce89123445d9f8075ee7cd38ee968a87630d7ec2f69355ca09325d"
                + "f14c9d89d081a3ade084cf08c2ab13e144998908fa41a06902e23fd410c4573135e41f4e"
                + "60e8e3f6e7ca96da202e274a63093a1372c70f1c82fb136fda1f72c3a096581ea32780888c4678f0c479a70b41a1f3ca";
        final String encodedAuth = Base64.getEncoder().encodeToString((AppID + ":" + AppSecret).getBytes());

        String url = buildUrl();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Basic " + encodedAuth);
        return RequestSender.sendRequest(request);
    }
}
