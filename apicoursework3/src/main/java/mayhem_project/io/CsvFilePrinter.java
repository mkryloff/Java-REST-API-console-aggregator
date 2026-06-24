package mayhem_project.io;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvFilePrinter
{
    public static void printFile(String filename, String sourceFilter)
    {
        RFC4180Parser parser = new RFC4180ParserBuilder().build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filename))
                .withCSVParser(parser)
                .build())
        {
            List<String[]> allLines = reader.readAll();
            if (allLines.isEmpty())
            {
                return;
            }
            String[] header = allLines.get(0);
            int sourceIndex = -1;
            for (int i = 0; i < header.length; i++)
            {
                if ("source".equalsIgnoreCase(header[i]))
                {
                    sourceIndex = i;
                    break;
                }
            }
            System.out.println(Arrays.toString(header));
            for (int i = 1; i < allLines.size(); i++)
            {
                String[] row = allLines.get(i);
                if (sourceFilter == null || sourceFilter.isEmpty() || row[sourceIndex].equalsIgnoreCase(sourceFilter))
                {
                    System.out.println(Arrays.toString(row));
                }
            }
        }
        catch (IOException | CsvException e)
        {
            System.err.println("Cannot read CSV file!");
        }
    }
}
