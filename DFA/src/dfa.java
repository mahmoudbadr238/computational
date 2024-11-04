public class dfa {
    private ReadFilestates rs;
    private ReadFileTable rt;
    dfa(String filename){
        rs = new ReadFilestates(filename);
        rt = new ReadFileTable(filename);
    }

}
