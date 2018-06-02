import java.util.*;
import java.io.*;

public class ReverseSum {
    public static void main(String [] args) { 
        List<Integer> rowAmount = new ArrayList<>();
        List<Integer> allNumbers = new ArrayList<>();
        List<Integer> rowSum = new ArrayList<>();
        List<Integer> colSum = new ArrayList<>();
        MyScanner in = new MyScanner(System.in);
        try {
            while (in.ready()) {
                Integer temp = in.nextInt();
                if (temp == null) {
                    break;
                }
                allNumbers.add(temp);
                while (rowAmount.size() - 1 < in.getCurLine()) {
                    rowAmount.add(0);
                    rowSum.add(0);
                }
                rowAmount.set(rowAmount.size() - 1, rowAmount.get(rowAmount.size() - 1) + 1);
                rowSum.set(rowSum.size() - 1, rowSum.get(rowSum.size() - 1) + temp);
                while (colSum.size() < rowAmount.get(rowAmount.size() - 1)) {
                    colSum.add(0);
                }
                colSum.set(rowAmount.get(rowAmount.size() - 1) - 1, 
                                         colSum.get(rowAmount.get(rowAmount.size() - 1) - 1) + temp);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            in.close();
        }
        while (rowAmount.size() < in.getCurLine()) {
            rowAmount.add(0);
            rowSum.add(0);
        }
        for (int i = 0, k = 0; i < rowAmount.size(); i++) {
            for (int j = 0; j < rowAmount.get(i); j++, k++) {
                System.out.print(colSum.get(j) + rowSum.get(i) - allNumbers.get(k) + " ");
            }
            System.out.println();
        }              
    }
}

class MyScanner { 
    private BufferedReader in;
    private int curLine;
    private char delim;
    private boolean isReady;
    private boolean isNewLine;
    
    MyScanner(InputStream stream) {
        in = new BufferedReader(new InputStreamReader(stream));
        curLine = 0;
        isReady = true;
        isNewLine = false;
    }
  
    void close() {
        try {
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    boolean ready() {
        return isReady;
    }
    
    String next() throws IOException {
        StringBuilder curString = new StringBuilder();
        if (isNewLine) {
            isNewLine = false;
            curLine++;
        }
        while (isReady) {
            Integer k = in.read();
            if (k == -1) {
                isReady = false;
                break;
            } 
            Character c = (char) k.intValue();
            if (c == ' ' || c == '\r') {
                break;
            }
            if (c == '\n') {
                isNewLine = true;
                break;
            }
            curString.append(c);
        }
        return curString.toString();
    }
    
    Integer nextInt() throws IOException {
        while (isReady) {
            try {
                return Integer.parseInt(next());
            } catch (NumberFormatException e) {
                //System.out.println(e.getMessage());
            }
        } 
        return null;
    }

    int getCurLine() {
        return curLine;
    }
}
