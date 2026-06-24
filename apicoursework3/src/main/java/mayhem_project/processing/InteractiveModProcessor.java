package mayhem_project.processing;

import mayhem_project.apis.ApiClient;
import mayhem_project.io.FileIO;
import mayhem_project.parallelProcessing.ParallelApiExecutor;

import java.util.Map;
import java.util.Scanner;

public class InteractiveModProcessor
{
    public static void userRun()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select APIs from the list:");
        System.out.println("1. weather");
        System.out.println("2. astronomy");
        System.out.println("3. stockMarket");
        System.out.print("Enter your preferences, sep by comma(e.g 1,2,3): ");
        String apiList = scanner.next();
        String[] apis = GeneralProcessor.parseApiPreferences(apiList);

        System.out.print("Enter your output file type (JSON or CSV): ");
        String outputFileType = scanner.next();

        System.out.print("Enter <A> if you want to add data to file or enter <N> if you want to create new file: ");
        boolean appendFlag = scanner.next().equalsIgnoreCase("A");

        System.out.println("Enter max amount of threads: ");
        Integer threadAmount = scanner.nextInt();

        System.out.println("Enter time interval of requesting APIs: ");
        Integer timeInterval = scanner.nextInt();

        Map<String, ApiClient> clients = GeneralProcessor.collectApiClients(apis);

        ParallelApiExecutor parallelProcessor = new ParallelApiExecutor(clients, outputFileType, threadAmount, timeInterval, appendFlag);

        System.out.println("Enter <start> to start APIs requesting");
        System.out.println("Enter <stop> to stop APIs requesting");
        System.out.println("Enter <exit> to exit program");
        scanner.nextLine();

        while (true)
        {
            switch (scanner.nextLine())
            {
                case "start" ->
                {
                    parallelProcessor.start();
                    System.out.println("Enter <stop> to stop APIs requesting");
                }
                case "stop" ->
                {
                    parallelProcessor.stop();
                    System.out.println("Enter <start> to start APIs requesting");
                    System.out.println("Or you can print out responses from file by entering <print>");
                }
                case "exit" ->
                {
                    if (parallelProcessor.getIsStarted())
                    {
                        parallelProcessor.stop();
                    }
                    return;
                }
                case "print" ->
                {
                    System.out.println("Select APIs from file to print on the screen:");
                    System.out.println("1. WeatherAPI");
                    System.out.println("2. AstronomyAPI");
                    System.out.println("3. stockMarketAPI");
                    System.out.print("Enter your preferences, sep by comma(e.g 1,2) or A to print entire file: ");
                    String apisToPrint = scanner.next();
                    String[] apisListToPrint = apisToPrint.split(",");
                    FileIO.printData(outputFileType, apisListToPrint);
                    scanner.nextLine();
                }
                default ->
                {
                    System.err.println("Incorrect command!");
                    System.out.println("Enter <start> to start APIs requesting");
                    System.out.println("Enter <stop> to stop APIs requesting");
                    System.out.println("Enter <exit> to exit program");
                }
            }
        }

    }
}



