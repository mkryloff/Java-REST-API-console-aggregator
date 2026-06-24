package mayhem_project.parallelProcessing;

import mayhem_project.apis.ApiClient;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelApiExecutor
{
    private ScheduledThreadPoolExecutor scheduler;
    private final Map<String, ApiClient> apiClients;
    private final String outputFileType;
    private final Integer threadAmount;
    private Integer timeInterval;
    private final boolean isAppend;
    private boolean isStarted;

    public ParallelApiExecutor(Map<String, ApiClient> apis, String outputFileType, Integer threadAmount, Integer timeInterval, boolean isAppend)
    {
        this.apiClients = apis;
        this.outputFileType = outputFileType;
        this.threadAmount = threadAmount;
        this.timeInterval = timeInterval;
        this.isAppend = isAppend;
        this.isStarted = false;
    }

    public void start()
    {
        if (isStarted)
        {
            System.err.println("Parallel requesting has already started!");
            return;
        }

        scheduler = new ScheduledThreadPoolExecutor(threadAmount);
        if (timeInterval <= 0)
        {
            System.err.println("Inappropriate time interval for requesting APIs: " + timeInterval + " seconds");
            this.timeInterval = 1;
            System.err.println("Time interval was switched to 1 second");
        }

        if (!isAppend)
        {
            File file = new File("result." + outputFileType.toLowerCase());
            if (file.exists())
            {
                boolean isDeleted =  file.delete();
                if (!isDeleted)
                {
                    System.err.println("ERROR: cannot delete old file");
                }
            }
        }

        for (ApiClient client : apiClients.values())
        {
            ApiTask task = new ApiTask(client, outputFileType, true);
            scheduler.scheduleWithFixedDelay(task, 0, timeInterval, TimeUnit.SECONDS);
        }

        this.isStarted = true;
        System.out.println("Requesting has started!");
    }

    public void stop()
    {
        if (!isStarted)
        {
            System.err.println("Parallel requesting has already been stopped or not started!");
            return;
        }
        scheduler.shutdown();
        try
        {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS))
            {
                scheduler.shutdownNow();
            }
        }
        catch (InterruptedException e)
        {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        finally
        {
            this.isStarted = false;
        }
        System.out.println("Requesting has stopped!");
    }

    public boolean getIsStarted()
    {
        return this.isStarted;
    }
}
