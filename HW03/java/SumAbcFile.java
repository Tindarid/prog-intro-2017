import java.util.*;
import java.io.*;

public class SumAbcFile {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Need 2 arguments");
            return;
        }
        int ans = 0;
        int temp = 0;
        try {
            InputStreamReader inputStr = new InputStreamReader(new FileInputStream(args[0]), "utf-8");
            Scanner in = new Scanner(inputStr);
            try {
                while (in.hasNext()) {
                    String s = in.next();
                    StringBuilder curString = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (Character.isLetter(c)) {
                            c = Character.toLowerCase(c);
                            c -= 'a';
                            c += '0';
                        }
                        curString.append(c);
                    }
                    try {
                        ans += Integer.parseInt(curString.toString());
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }                         
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
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8");
            try {
                out.write(ans + "");
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
