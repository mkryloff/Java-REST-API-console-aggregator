package mayhem_project.parallelProcessing;

import mayhem_project.ApiDataStorage;
import mayhem_project.ResponseParser;
import mayhem_project.apis.ApiClient;
import mayhem_project.io.FileIO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.Mockito.*;

class ApiTaskTest
{

    @Test
    void run_WhenResponseNotNull_CallsFileIOWriteData()
    {
        ApiClient client = mock(ApiClient.class);
        when(client.getSourceName()).thenReturn("testApi");
        when(client.getStringResponse()).thenReturn("{\"key\":\"value\"}");

        ApiDataStorage mockRecord = mock(ApiDataStorage.class);
        try (MockedStatic<ResponseParser> parserMock = mockStatic(ResponseParser.class);
             MockedStatic<FileIO> fileIOMock = mockStatic(FileIO.class))
        {

            parserMock.when(() -> ResponseParser.parseResponseToObject("testApi", "{\"key\":\"value\"}")).thenReturn(mockRecord);

            ApiTask task = new ApiTask(client, "json", true);
            task.run();

            fileIOMock.verify(() -> FileIO.writeData(List.of(mockRecord), "json", true), times(1));
        }
    }

    @Test
    void run_WhenResponseNull_DoesNotCallFileIO()
    {
        ApiClient clientMock = mock(ApiClient.class);
        when(clientMock.getSourceName()).thenReturn("testApi");
        when(clientMock.getStringResponse()).thenReturn(null);

        try (MockedStatic<ResponseParser> parserMock = mockStatic(ResponseParser.class);
             MockedStatic<FileIO> fileIOMock = mockStatic(FileIO.class))
        {
            parserMock.when(() -> ResponseParser.parseResponseToObject("testApi", null)).thenReturn(null);

            ApiTask task = new ApiTask(clientMock, "json", true);
            task.run();

            parserMock.verify(() -> ResponseParser.parseResponseToObject("testApi", null), times(1));
            fileIOMock.verifyNoInteractions();
        }
    }

    @Test
    void run_WhenParseReturnsNull_DoesNotCallFileIO()
    {
        ApiClient client = mock(ApiClient.class);
        when(client.getSourceName()).thenReturn("testApi");
        when(client.getStringResponse()).thenReturn("{}");

        try (MockedStatic<ResponseParser> parserMock = mockStatic(ResponseParser.class);
             MockedStatic<FileIO> fileIOMock = mockStatic(FileIO.class))
        {
            parserMock.when(() -> ResponseParser.parseResponseToObject("testApi", "{}")).thenReturn(null);

            ApiTask task = new ApiTask(client, "json", true);
            task.run();
            fileIOMock.verifyNoInteractions();
        }
    }
}