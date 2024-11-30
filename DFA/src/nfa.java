import java.io.IOException;
import java.util.*;

public class nfa {
    private ReadFilestates rs;
    private ReadFileTable rt;
    private String[][] states;
    private String[][] transitionTable;
    private String startState;
    private String[] acceptStates;

    public nfa(String formula , String tt) throws IOException {
        rs = new ReadFilestates(formula);
        rt = new ReadFileTable(tt);
        intialize_nfa();
    }
    private void intialize_nfa() throws IOException {
        states= rs.call();
        transitionTable= rt.call();
        printState();
        printTable();
        assert states != null;
        startState=states[2][0];
        acceptStates=states[3];
        System.out.println("Initialization complete:");
        System.out.println("Start State: " + startState);
        System.out.print("Accept States: ");
        assert acceptStates != null;
        for (String acceptState : acceptStates) {
            System.out.print(acceptState + " ");
        }
        System.out.println();
    }
    private void printState() {
        System.out.println("States:");
        for (String[] state : states) {
            for (String s : state) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }
    private void printTable() {
        System.out.println("Transition Table:");
        for (String[] row : transitionTable) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
    public boolean process(String input) {
        Set<String> currentstate=epsilonClosure(Collections.singleton(startState));
        for (char c : input.toCharArray()) {
            System.out.println("current states:" + currentstate + ", symbol: " + c);
            Set<String> nextStates =new HashSet<>();
            for (String state : currentstate) {
                Set<String> transitions = getNextStates(state,c);
                if (transitions != null) {
                    nextStates.addAll(transitions);
                }
            }
            currentstate=epsilonClosure(nextStates);
            if(currentstate.isEmpty()) {
                System.out.println("No valid transitions found");
                return false;
            }
            System.out.println("nextStates: " + currentstate);
        }
        boolean isAccepted=isAnyAcceptState(currentstate);
        System.out.println("Final state: " + currentstate + ", isAccepted: " + isAccepted);
        return isAccepted;
    }
    private Set<String> getNextStates(String state, char c) {
        int stateIndex = findstateIndex(state);
        int symbolIndex = findSymbolIndex(String.valueOf(c));
        if(stateIndex == -1 || symbolIndex == -1 ) {
            System.out.println("Invalid transition: state = " + state + ", symbol = " + c);
            return null;
        }
        String transition = transitionTable[stateIndex][symbolIndex];
        System.out.println("Transition for state " + state + ", symbol " + c + " -> " + transition);
        if (transition.isEmpty()||transition.equalsIgnoreCase("_")) {
            return null;
        }
        return new HashSet<>(Arrays.asList(transition.split(" ")));
    }
    private int findstateIndex(String state) {
        for (int i = 0; i < transitionTable.length; i++) {
            if (transitionTable[i][0].equals(state)) {
                return i;
            }
        }
        System.out.println("state not found "+state);
        return -1;
    }
    private int findSymbolIndex(String symbol) {
        for (int i = 0; i < transitionTable[0].length; i++) {
            if (transitionTable[0][i].equals(symbol)) {
                return i;
            }
        }
        System.out.println("symbol not found "+symbol);
        return -1;
    }
    private boolean isAnyAcceptState(Set<String> states) {
        for (String state : states) {
            for (String acceptState : acceptStates) {
                if (state.equals(acceptState)) {
                    return true;
                }
            }
        }
        return false;
    }
    private Set<String> epsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);
        while (!stack.isEmpty()) {
            String s = stack.pop();
            Set<String> epsilon = getNextStates(s,'ε');
            if (epsilon != null) {
                for (String epsilonState : epsilon) {
                    if(!closure.contains(epsilonState)) {
                        closure.add(epsilonState);
                        stack.push(epsilonState);
                    }
                }
            }
        }
        System.out.println("Epsilon clousre for states "+states+"->"+closure);
        return closure;
    }
}
