package mayhem_project.parallelProcessing;

import mayhem_project.ResponseParser;
import mayhem_project.apis.ApiClient;
import mayhem_project.io.FileIO;

import java.util.List;
import java.util.Optional;

public record ApiTask(ApiClient client, String outputFileType, boolean isAppend) implements Runnable
{
    @Override
    public void run()
    {
        String response = client.getStringResponse();
        Optional.ofNullable(ResponseParser.parseResponseToObject(client.getSourceName(), response))
                .ifPresent(data -> FileIO.writeData(List.of(data), outputFileType, isAppend));
    }
}
