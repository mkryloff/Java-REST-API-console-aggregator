package mayhem_project.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import mayhem_project.ApiDataStorage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class CsvDataWriter
{
    public static synchronized void saveDataToFile(List<ApiDataStorage> newData, String filename, boolean append)
    {
        if (newData == null || newData.isEmpty())
        {
            return;
        }

        List<ApiDataStorage> allData = new ArrayList<>();
        File file = new File(filename);
        if (append && file.exists())
        {
            List<ApiDataStorage> existing = readAll(filename);
            allData.addAll(existing);
        }
        allData.addAll(newData);
        writeAll(allData, filename);
    }

    private static void writeAll(List<ApiDataStorage> data, String filename)
    {
        if (data.isEmpty())
        {
            return;
        }

        Set<String> allPaths = new LinkedHashSet<>();
        List<Map<String, String>> rowsData = new ArrayList<>();

        for (ApiDataStorage unit : data)
        {
            String source = unit.source();
            JsonNode dataNode = unit.data();

            Map<String, String> flat = flattenJson("", dataNode);
            Map<String, String> prefixed = new HashMap<>();
            for (Map.Entry<String, String> field : flat.entrySet())
            {
                String key = source + "_" + field.getKey();
                prefixed.put(key, field.getValue());
                allPaths.add(key);
            }
            rowsData.add(prefixed);
        }

        List<String> header = new ArrayList<>();
        header.add("Id");
        header.add("source");
        header.add("timestamp");
        header.addAll(allPaths);

        try (CSVWriter writer = new CSVWriter(new FileWriter(filename, false)))
        {
            writer.writeNext(header.toArray(new String[0]));
            for (int i = 0; i < data.size(); i++)
            {
                ApiDataStorage unit = data.get(i);
                Map<String, String> rowMap = rowsData.get(i);

                List<String> row = new ArrayList<>();
                row.add(unit.id());
                row.add(unit.source());
                row.add(unit.timestamp().toString());

                for (String path : allPaths)
                {
                    String value = rowMap.getOrDefault(path, "");
                    row.add(value);
                }
                writer.writeNext(row.toArray(new String[0]));
            }
        }
        catch (IOException e)
        {
            System.err.println("ERROR: cannot write data into CSV file");
        }
    }


    private static Map<String, String> flattenJson(String prefix, JsonNode node)
    {
        Map<String, String> result = new LinkedHashMap<>();
        if (node.isObject())
        {
            node.fields().forEachRemaining(unit ->
            {
                String newPrefix = prefix.isEmpty() ? unit.getKey() : prefix + "." + unit.getKey();
                result.putAll(flattenJson(newPrefix, unit.getValue()));
            });
        }
        else if (node.isArray())
        {
            result.put(prefix, node.toString());
        }
        else if (node.isValueNode())
        {
            result.put(prefix, node.asText());
        }
        else if (node.isNull())
        {
            result.put(prefix, "");
        }
        return result;
    }

    public static List<ApiDataStorage> readAll(String filename)
    {
        List<ApiDataStorage> result = new ArrayList<>();
        RFC4180Parser parser = new RFC4180ParserBuilder().build();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filename))
                .withCSVParser(parser)
                .build())
        {
            List<String[]> lines = reader.readAll();
            if (lines.size() < 2)
            {
                return result;
            }
            String[] header = lines.get(0);
            for (int i = 1; i < lines.size(); i++)
            {
                String[] row = lines.get(i);
                if (row.length == 0)
                {
                    continue;
                }
                String id = row[0];
                String source = row[1];
                Instant timestamp = Instant.parse(row[2]);
                ObjectNode data = new ObjectMapper().createObjectNode();
                for (int j = 3; j < header.length && j < row.length; j++)
                {
                    String colName = header[j];
                    if (colName.startsWith(source + "_"))
                    {
                        String fieldName = colName.substring(source.length() + 1);
                        data.put(fieldName, row[j]);
                    }
                }
                result.add(new ApiDataStorage(id, source, timestamp, data));
            }
        }
        catch (CsvException | IOException e)
        {
            System.err.println("ERROR: cannot read data from CSV file" + e.getMessage());
        }
        return result;
    }
}
