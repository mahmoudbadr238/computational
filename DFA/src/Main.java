import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);
        while(true) {
        System.out.println("enter the choice you want");
        System.out.println("1.DFA");
        System.out.println("2.NFA");
        System.out.println("3.EXIT");
        System.out.print("Your choice: ");
        String select= scanner.nextLine();
            if (select.equalsIgnoreCase("1")) {
                String formulaFileName = JOptionPane.showInputDialog("Enter formula file name");
                String tableFileName = JOptionPane.showInputDialog("Enter table file name");
                try {
                    dfa dfaInstance = new dfa(formulaFileName, tableFileName);

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
                }
            }
            else if (select.equalsIgnoreCase("2")) {
                try {
                    String formulaFileName = JOptionPane.showInputDialog("Enter formula file name");
                    String tableFileName = JOptionPane.showInputDialog("Enter table file name");
                    nfa nfaInstance = new nfa(formulaFileName, tableFileName);
                    while (true) {
                        System.out.println("Enter a string to test (or type 'exit' to quit): ");
                        String input = scanner.nextLine();

                        if ("exit".equalsIgnoreCase(input)) {
                            System.out.println("Exiting...");
                            break;
                        }

                        boolean isAccepted = nfaInstance.process(input);
                        if (isAccepted) {
                            System.out.println("The input string \"" + input + "\" is accepted by the NFA.");
                        } else {
                            System.out.println("The input string \"" + input + "\" is rejected by the NFA.");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading NFA configuration files: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            else if (select.equalsIgnoreCase("3")) {
                System.out.println("Exiting.....");
                break;
            }
            else {
                System.out.println("wrong input");

            }
        }
        scanner.close();
    }
}
