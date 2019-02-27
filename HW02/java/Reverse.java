import java.util.*;
import java.io.*;

public class Reverse {
    public static void main(String[] args) {
        List<Integer> allNumbers = new ArrayList<>();
        List<Integer> numbersOnLine = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        try {
            while (in.hasNextLine()) {
                int count = 0;
                String s = in.nextLine();
                StringTokenizer strTok = new StringTokenizer(s);
                while (strTok.hasMoreTokens()) {
                    try {
                        allNumbers.add(Integer.parseInt(strTok.nextToken()));
                        count++;
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                }
                numbersOnLine.add(count);
            }
        } catch (NoSuchElementException e) {
            //System.out.println(e.getMessage());
        } finally {
            in.close();
        }
        int i = allNumbers.size() - 1;
        for (int j = numbersOnLine.size() - 1; j >= 0; j--) {
            for (int k = 0; k < numbersOnLine.get(j); k++) {
                System.out.print(allNumbers.get(i) + " ");
                i--;
            }
            System.out.println();
        }
    }
}
