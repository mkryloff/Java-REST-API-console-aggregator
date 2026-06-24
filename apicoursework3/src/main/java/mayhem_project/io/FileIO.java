package mayhem_project.io;

import mayhem_project.ApiDataStorage;

import java.util.List;

public class FileIO
{
    public static void writeData(List<ApiDataStorage> data, String extension, boolean append)
    {
        switch (extension.toLowerCase())
        {
            case "json" -> JsonDataWriter.saveDataToFile(data, "result." + extension.toLowerCase(), append);
            case "csv" -> CsvDataWriter.saveDataToFile(data, "result." + extension.toLowerCase(), append);
            default -> System.err.println("Incorrect type of output file: " + extension.toLowerCase());
        }
    }

    public static void printData(String extension, String[] filters)
    {
        for (String token : filters)
        {
            String sourceFilter = convertFilter(token);
            if (sourceFilter == null)
            {
                System.err.println("Incorrect print filter: " + token);
                continue;
            }
            switch (extension.toLowerCase())
            {
                case "json" -> JsonFilePrinter.printFile("result." + extension.toLowerCase(), sourceFilter);
                case "csv" -> CsvFilePrinter.printFile("result." + extension.toLowerCase(), sourceFilter);
                default -> System.err.println("Incorrect type of output file: " + extension.toLowerCase());
            }
        }
    }

    private static String convertFilter(String filter)
    {
        return switch (filter)
        {
            case "A", "a" -> "";
            case "1" -> "weatherApi";
            case "2" -> "astronomyApi";
            case "3" -> "stockMarketApi";
            default -> null;
        };
    }
}
