import java.util.*;
import java.io.*;

public class WordStatLineIndex {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Need 2 files");
            return;
        }
        Map<String, List<RowIndex>> wordsStat = new TreeMap<>();
        int globalCount = 1;
        try {
            Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream (args[0]), "utf-8")));
            try {             
                while (in.hasNextLine()) {
                    int localCount = 1;
                    String[] tokens = in.nextLine().split("[^\\p{Pd}\\p{L}\']+");
                    for (String token : tokens) {
                        String s = token.toLowerCase();
                        if (s.length() > 0) {
                            if (!wordsStat.containsKey(s)) {
                                wordsStat.put(s, new ArrayList<RowIndex>());
                            }
                            wordsStat.get(s).add(new RowIndex(globalCount, localCount));
                            localCount++;
                        }
                    }
                    globalCount++;
                }
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter (new FileOutputStream (args[1]), "utf-8"));
            try {
                for (Map.Entry<String, List<RowIndex>> entry : wordsStat.entrySet()) {
                    out.write(entry.getKey() + " " + entry.getValue().size());
                    for (int i = 0; i < entry.getValue().size(); i++)
                        out.write(" " + entry.getValue().get(i).row + ":" + entry.getValue().get(i).number);
                    out.write("\n");
                }
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    static class RowIndex {
        public int row;
        public int number;
        
        RowIndex(int r, int n) {
            row = r;
            number = n;
        }
    }
}
