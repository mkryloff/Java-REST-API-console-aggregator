package mayhem_project.processing;

import mayhem_project.apis.ApiClient;
import mayhem_project.parallelProcessing.ParallelApiExecutor;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;


public class AutoModProcessor
{
    public static void autoRun(String[] cliArgs)
    {
        String[] apis = parseCliArguments(Arrays.copyOfRange(cliArgs, 1, cliArgs.length - 3));
        Map<String, ApiClient> clients = GeneralProcessor.collectApiClients(apis);

        ParallelApiExecutor parallelProcessor = new ParallelApiExecutor(clients, cliArgs[cliArgs.length - 3],
                Integer.valueOf(cliArgs[cliArgs.length - 2]), Integer.valueOf(cliArgs[cliArgs.length - 1]), false);

        parallelProcessor.start();

        try (Scanner scanner = new Scanner(System.in))
        {
            scanner.nextLine();
            parallelProcessor.stop();
        }

    }

    private static String[] parseCliArguments(String[] apiList)
    {
        StringBuilder apiString = new StringBuilder();
        for (String str : apiList)
        {
            apiString.append(str);
        }
        return apiString.toString().split(",");
    }

}
