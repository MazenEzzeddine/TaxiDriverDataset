import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParseDataset {

    static String[] arrival_date = new String[414983];
    static Date[] arrival_javadate = new Date[414983];

    private static final boolean DRY_RUN = true;


    private static Map<Integer, Long> arrivalRatePerHour = new HashMap<>();


    public static void main(String[] args) throws IOException, PythonExecutionException {
        parseFile();
        writecsv();

    }


    private static void parseFile() throws IOException {

        for (int i = 0; i < 24; i++)
            arrivalRatePerHour.put(i, 0L);
        int count = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boolean header = true;

        try (CSVReader reader = new CSVReader(new FileReader("trip_data_3.csv"))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                System.out.println("Line " + count);
                arrival_date[count] = lineInArray[5];
                arrival_javadate[count] = formatter.parse(arrival_date[count]);

                System.out.println(arrival_date[count]);
                System.out.println(arrival_javadate[count]);
                System.out.println("Hours " + arrival_javadate[count].getHours());
                System.out.println("Minutes " + arrival_javadate[count].getMinutes());
                System.out.println("Seconds " + arrival_javadate[count].getSeconds());

                arrivalRatePerHour.put(arrival_javadate[count].getHours(),
                        arrivalRatePerHour.get(arrival_javadate[count].getHours()) + 1);

                System.out.println("==========================");
                count++;

                // if(count == 414983) break;
                //one day only
                if (count == 198372) break;

            }
        } catch (CsvValidationException | ParseException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 24; i++)
            System.out.println(arrivalRatePerHour.get(i));


    }


    private static void writecsv() throws IOException {
        FileOutputStream fos = new FileOutputStream("rates.csv");
        OutputStreamWriter osw = new OutputStreamWriter(fos);

        List<String[]> csvs = new ArrayList<>();

        for (Integer e : arrivalRatePerHour.keySet()) {
            String[] s = new String[2];
            s[0] = e.toString();
            s[1] = arrivalRatePerHour.get(e).toString();
            csvs.add(s);
        }
        CSVWriter csvw = new CSVWriter(osw);
        csvw.writeAll(csvs);

        csvw.close();


    }
}
