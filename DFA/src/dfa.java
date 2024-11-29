import java.io.IOException;

public class dfa {
    private ReadFilestates rs;
    private ReadFileTable rt;
    private String[][] states;
    private String[][] transitionTable;
    private String startState;
    private String[] acceptStates; 


      // read both formula and table 


    public dfa(String configFileName, String tableFileName) throws IOException {
        rs = new ReadFilestates(configFileName);
        rt = new ReadFileTable(tableFileName);
        initializeDFA();
    }

    // try run and we will understand what this fundtion do  just printing states and transition table after cleaning and filtering 
    // and the basic information about the sitution start , final ...... 

    private void initializeDFA() throws IOException {
        states = rs.call();
        transitionTable = rt.call();
        
        
        printStates();
        printTransitionTable();

        startState = states[2][0];  
        acceptStates = states[3];    

        System.out.println("Initialization complete:");
        System.out.println("Start State: " + startState);
        System.out.print("Accept States: ");
        for (String acceptState : acceptStates) {
            System.out.print(acceptState + " ");
        }
        System.out.println();
    }


    private void printStates() {
        System.out.println("States:");
        for (String[] state : states) {
            for (String s : state) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }

    private void printTransitionTable() {
        System.out.println("Transition Table:");
        for (String[] row : transitionTable) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    public boolean process(String input) {

        // we get the start state ex q1 and we starting inputs with 0


        String currentState = startState;
        System.out.println("\nStarting DFA processing for input: " + input); // ex Starting DFA processing for input: 0 
      
        // parsing the input ex 0000111 .....  each char will enter the loop and begin the process   

        for (char symbol : input.toCharArray()) {
            System.out.println("Current State: " + currentState + ", Symbol: " + symbol);
            String nextState = getNextState(currentState, symbol);
            
            if (nextState == null) {
                System.out.println("Transition not found. Input rejected.");
                return false;
            }

            System.out.println("Transition from " + currentState + " to " + nextState + " on symbol " + symbol);
            currentState = nextState;
            System.out.println("Transitioning to Next State: " + currentState);
        }

        boolean isAccepted = isAcceptState(currentState);
        System.out.println("Final State: " + currentState + ", Accepted: " + isAccepted);
        return isAccepted;
    }
     

    // i send the current state ex q1 and the sympol ex 0 and i firstlu go to function find state state index go there 
     
    // and then go findSymbolIndex  to know which state the current state go with this symbol go and see what's happening there 


    private String getNextState(String currentState, char symbol) {
        int stateIndex = findStateIndex(currentState);

        int symbolIndex = findSymbolIndex(String.valueOf(symbol));

        if (stateIndex == -1 || symbolIndex == -1) {
            System.out.println("Error: Invalid state or symbol.");
            return null; 
        }

        return transitionTable[stateIndex][symbolIndex]; 
    }
   
    // just changing the rows and making the colums fixed because
    // i want only the states which are in the first column see the filtered array and you will get it 

    private int findStateIndex(String state) {
        System.out.println(state);
        printTransitionTable();
        for (int i = 0; i < transitionTable.length; i++) {

            System.out.println(transitionTable[i][0]);

            if (transitionTable[i][0].equals(state)) {
                System.out.println(transitionTable[i][0]);
                return i;
            }
        }
        System.out.println("State " + state + " not found in states array.");
        return -1; 
    }

    // the opposite of finding state 
 
    private int findSymbolIndex(String symbol) {
        for (int i = 0; i < transitionTable[0].length; i++) {
            if (transitionTable[0][i].equals(String.valueOf(symbol))) {
                return i;
            }
        }
        System.out.println("Symbol " + symbol + " not found in transition table.");
        return -1; 
    }

    private boolean isAcceptState(String state) {
        for (String acceptState : acceptStates) {
            if (acceptState.equals(state)) {
                return true;
            }
        }
        return false;
    }
}
