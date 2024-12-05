import java.io.IOException;
import java.util.*;
public class nfa {
    private ReadFilestates rs;
    private ReadFileTable rt;
    private String[][] states;
    private String[][] transitionTable;
    private String startState;
    private String[] acceptStates;
    private Set<String> acceptStatesSet;
    private Map<String,Set<String>> epsilonTransitions;

    public nfa(String formula , String tt) throws IOException {
        rs = new ReadFilestates(formula);
        rt = new ReadFileTable(tt);
        epsilonTransitions =new HashMap<>();
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
        acceptStatesSet=new HashSet<>(Arrays.asList(acceptStates));
        System.out.println("Initialization complete:");
        System.out.println("Start State: " + startState);
        System.out.print("Accept States: ");
        assert acceptStates != null;
        for (String acceptState : acceptStates) {
            System.out.print(acceptState + " ");
        }
        System.out.println();
        loadEpsilonTransitions();
    }
    private void loadEpsilonTransitions(){
        for (int i=1;i<transitionTable.length;i++){
            String currentState=transitionTable[i][0];
            Set<String> epsilonStates =new HashSet<>();
            String epsilonTransition =transitionTable[i][transitionTable[i].length-1];
            if(epsilonTransition !=null && !epsilonTransition.isEmpty() && !epsilonTransition.equals("_")){
                epsilonStates.addAll(Arrays.asList(epsilonTransition.split(" ")));
            }
            if(!epsilonStates.isEmpty()){
                epsilonTransitions.put(currentState,epsilonStates);
            }
        }
        for(Map.Entry<String,Set<String>> entry:epsilonTransitions.entrySet()){
            System.out.println(entry.getKey()+" -> "+entry.getValue());
        }
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
        if(input.startsWith(" ")){
            input="ε"+input.substring(1);
        }
        Set<String> currentstate=epsilonClosure(Collections.singleton(startState));
        List<List<String>> paths=new ArrayList<>();
        paths.add(new ArrayList<>(Collections.singleton(startState)));
        currentstate =epsilonClosure(currentstate);
        System.out.println("Initial epsilon closure: "+ currentstate);
        for (char c : input.toCharArray()) {
            System.out.println("current states:" + currentstate + ", symbol: " + c);
            List<List<String>> newPaths = new ArrayList<>();
            Set<String> nextStates =new HashSet<>();
            for (String state : currentstate){
                Set<String> transitions = getNextStates(state,c);
                if (transitions != null){
                    nextStates.addAll(transitions);
                    for (List<String> path :paths){
                        if (path.get(path.size()-1).equals(state)){
                            for (String nextState : transitions){
                                List<String> newPath = new ArrayList<>(path);
                                newPath.add(nextState);
                                newPaths.add(newPath);
                            }
                        }
                    }
                }
            }
            currentstate=epsilonClosure(nextStates);
            paths = updatePathsForEpsilonClosure(newPaths);
            if(currentstate.isEmpty()) {
                System.out.println("No valid transitions found");
                markAcceptedPaths(paths);
                return false;
            }
            printAllPaths(paths);
            System.out.println("nextStates: " + currentstate);
        }
        currentstate=epsilonClosure(currentstate);
        return markAcceptedPaths(paths);
    }
    private Set<String> getNextStates(String state, char c) {
        int stateIndex = findstateIndex(state);
        int symbolIndex = findSymbolIndex(String.valueOf(c));
        if(stateIndex == -1 || symbolIndex == -1 ) {
            System.out.println("Invalid transition: state = " + state + ", symbol = " + c);
            return null;
        }
        if(c == 'ε'){
            return getEpsilonTransition(state);
        }
        String transition = transitionTable[stateIndex][symbolIndex];
        if (transition == null || transition.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> nextStates = new HashSet<>(Arrays.asList(transition.split(" ")));
        return nextStates;
    }
    private Set<String> getEpsilonTransition(String state) {
        Set<String> epsilonStates = epsilonTransitions.get(state);
        if (epsilonStates == null) {
            return Collections.emptySet();
        }
        return epsilonStates;
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
    private Set<String> epsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        for (String state :states){
            stack.push(state);
        }
        while (!stack.isEmpty()) {
            String state = stack.pop();
            Set<String> epsilonStates =getEpsilonTransition(state);
            for (String epsilonState : epsilonStates){
                if (closure.add(epsilonState)) {
                    stack.push(epsilonState);
                }
            }
        }
        return closure;
    }
    private List<List<String>> updatePathsForEpsilonClosure(List<List<String>> paths) {
        List<List<String>> updatedPaths = new ArrayList<>();
        for (List<String> path : paths) {
            String lastState=path.get(path.size()-1);
            Set<String>closure=epsilonClosure(Collections.singleton(lastState));
            for (String closureState : closure) {
                List<String> newPath =new ArrayList<>(path);
                if(!newPath.contains(closureState)){
                    newPath.add(closureState);
                }
                updatedPaths.add(newPath);
            }
        }
        return updatedPaths.isEmpty() ? paths : updatedPaths;
    }
    private void printAllPaths(List<List<String>> paths){
        System.out.println("paths so far");
        int index = 1;
        for (List<String> path :paths){
            System.out.println(index + " : "+path);
            index++;
        }
    }
    private boolean markAcceptedPaths(List<List<String>>paths){
        boolean foundAccepted=false;
        for(List<String> path:paths){
            String laststate =path.get(path.size()-1);
            if(acceptStatesSet.contains(laststate)){
                System.out.println(path + "accepted");
                foundAccepted =true;
            }
        }
        if(!foundAccepted){
            System.out.println("no path leads to accept state");
        }
        return foundAccepted;
    }
}