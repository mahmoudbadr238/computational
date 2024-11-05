import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileTable {

    private String fileName;
    private String[][] arr;

    public ReadFileTable(String fileName) {
        this.fileName = fileName;
    }

    private void get_lines() throws IOException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {

            long lineCount = buffer.lines().count();
            arr = new String[(int) lineCount][];
        }
    }

    private void split_lines() throws IOException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
            while ((line = buffer.readLine()) != null) {
                line = line.trim();
                String[] elements = line.split("\\s+"); 
                arr[lineNumber] = elements;
                lineNumber++;
            }
        }
    }

    public String[][] call() throws IOException {
        get_lines();
        split_lines();
        return arr;
    }
}
