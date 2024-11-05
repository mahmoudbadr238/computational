import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);

         // shof dynamic path ezay grbt bs fslt elsara7a aw n5yrha 3 7sb elghaz ele hy run 
         
         // el file da shl msh m7tag shr7
         
        String formulaFileName = JOptionPane.showInputDialog("Enter formula file name");
        String tableFileName = JOptionPane.showInputDialog("Enter table file name");

        try {
            dfa dfaInstance = new dfa(formulaFileName,tableFileName);

            while (true) {
                System.out.println("Enter a string to test (or type 'exit' to quit): ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Exiting...");
                    break;
                }

                boolean isAccepted = dfaInstance.process(input);
                if (isAccepted) {
                    System.out.println("The input string \"" + input + "\" is accepted by the DFA.");
                } else {
                    System.out.println("The input string \"" + input + "\" is rejected by the DFA.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DFA configuration files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
