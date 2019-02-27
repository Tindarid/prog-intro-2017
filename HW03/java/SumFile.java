import java.util.*;
import java.io.*;

public class SumFile {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Need 2 arguments");
            return;
        }
        int ans = 0;
        try {
            Scanner in = new Scanner(new InputStreamReader(new FileInputStream(args[0]), "utf-8"));
            try {
                while (in.hasNextInt()) {
                    ans += in.nextInt();
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
