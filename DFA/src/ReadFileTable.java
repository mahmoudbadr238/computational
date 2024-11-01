import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadFileTable {

    private String fileName;
    private int line_count_i;
    private String[][] arr;

    public ReadFileTable(String fileName) {
        this.fileName = fileName;
    }

    private void get_lines() throws FileNotFoundException, IOException {
        BufferedReader Buffer = new BufferedReader(new FileReader(fileName));
        long line_count = Buffer.lines().count();
        line_count_i = (int) line_count;
        Buffer.close();
    }

    private void split_lines() throws FileNotFoundException, IOException {
        BufferedReader Buffer = new BufferedReader(new FileReader(fileName));
        int line_number = 0;
        this.arr = new String[line_count_i][];
        String line;
        while ((line = Buffer.readLine()) != null) {
            line = line.trim();
            String[] elements = line.split(" ");
            arr[line_number] = elements;
            line_number++;
        }
        Buffer.close();
    }

    public String[][] call() throws FileNotFoundException, IOException {
        get_lines();
        split_lines();
        return arr;
    }
}