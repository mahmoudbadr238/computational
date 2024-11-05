import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFilestates {

    private String fileName;
    private String[][] arr;

    public ReadFilestates(String fileName) {
        this.fileName = fileName;
    }

    private void get_lines() throws IOException {
        // just for error hundling wkda fzlka dworry:""
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {

            long lineCount = buffer.lines().count();
            arr = new String[(int) lineCount][];


        }
    }

    /// just re arranging code the same structure bta3k 
    private void split_lines() throws IOException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
            while ((line = buffer.readLine()) != null) {
                line = line.trim();
                String[] elements = line.split("\\s+"); // kol word lw7dha bs ashl mn your way 
                arr[lineNumber] = elements;
                lineNumber++;
            }
        }
    }

    private void edit_lines() throws FileNotFoundException, IOException {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j].equalsIgnoreCase("{") || arr[i][j].equalsIgnoreCase("}") || arr[i][j].equalsIgnoreCase(",")) {
                    arr[i][j] = null; 
                }
            }
        }
    }

    private void filter() {
       
        // el valid row  counting  lw msh null ++ 

        int validRowCount = 0;
        for (String[] row : arr) {
            int validCount = 0;
            for (String elem : row) {
                if (elem != null) {
                    validCount++;
                }
            }
            if (validCount > 0) {
                validRowCount++;
            }
        }

        //creating filtered array

        String[][] filtered = new String[validRowCount][];
        int filteredRowIndex = 0;


        //  just suring that both of them are equal 

        for (String[] row : arr) {
            int validCount = 0;
            for (String elem : row) {
                if (elem != null) {
                    validCount++;
                }
            }
            if (validCount > 0) {
                String[] newRow = new String[validCount];
                int newColIndex = 0;
                for (String elem : row) {
                    if (elem != null) {
                        newRow[newColIndex++] = elem;
                    }
                }
                filtered[filteredRowIndex++] = newRow;
            }
        }

        arr = filtered; 
    }

    public String[][] call() throws IOException {
        get_lines();
        split_lines();
        edit_lines(); 
        filter(); 
        return arr;
    }
}
