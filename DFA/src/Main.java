import javax.swing.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

public class Main {
    public static void main(String[] args) throws IOException {
        String formula = JOptionPane.showInputDialog("enter the path of the formula file");
        ReadFilestates readFilestates = new ReadFilestates(formula);
        String[][] ar = readFilestates.call();
        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                System.out.print(ar[i][j] + " ");
            }
        }
    }
}