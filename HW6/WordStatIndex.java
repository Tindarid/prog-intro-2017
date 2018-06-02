import java.util.*;
import java.io.*;

public class WordStatIndex {
    public static void main(String [] args) {
        if (args.length != 2) {
            System.out.println("Need 2 files");
            return;
        }
        Map<String, ArrayList<Integer>> wordsStat = new LinkedHashMap<>();
        int globalCount = 0;
        try {
            Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream (args[0]), "utf-8")));
            try {
                in.useDelimiter("[^\\p{Pd}\\p{L}\']+");
                while (in.hasNext()) {
                    String s = in.next();
                    globalCount++;
                    s = s.toLowerCase();
                    if (!wordsStat.containsKey(s)) {
                        wordsStat.put(s, new ArrayList<Integer>());
                    }
                    wordsStat.get(s).add(globalCount);
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
            OutputStreamWriter out = new OutputStreamWriter (new FileOutputStream (args[1]), "utf-8");
            try {
                for (Map.Entry<String, ArrayList<Integer>> entry : wordsStat.entrySet()) {
                    out.write(entry.getKey() + " " + entry.getValue().size());
                    for (int i = 0; i < entry.getValue().size(); i++)
                        out.write(" " + entry.getValue().get(i));
                    out.write("\n");
                }
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
