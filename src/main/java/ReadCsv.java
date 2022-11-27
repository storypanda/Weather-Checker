import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.List;

public class ReadCsv {
    public static List<String[]> readAllDataAtOnce(String file)
    {
        List<String[]> allData = null;
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            allData = csvReader.readAll();
            return allData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }
}
