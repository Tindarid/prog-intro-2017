import java.util.*;

public class ReverseMax {
    public static void main(String args[]) {
        List<ArrayList<Integer>> allNumbers = new ArrayList<>();
        List<ArrayList<Integer>> maxs = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        ArrayList<String> lines = new ArrayList<String>();

        try {
            while (in.hasNextLine()) {
                ArrayList<Integer> numbers = new ArrayList<Integer>();
                Scanner inNumbers = new Scanner(in.nextLine());
                while (inNumbers.hasNextInt()) {
                    numbers.add(inNumbers.nextInt());
                }
                allNumbers.add(numbers);
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        } finally {
            in.close();
        }

        int maxColum = -1;
        for (int i = 0; i < allNumbers.size(); i++) {
            ArrayList<Integer> linemax = new ArrayList<>();
            if (!allNumbers.get(i).isEmpty()) {
                int max = Collections.max(allNumbers.get(i));
                for (int j = 0; j < allNumbers.get(i).size(); j++) {
                    linemax.add(max);
                }
            }
            maxs.add(linemax);
            if (maxColum < allNumbers.get(i).size()) {
                maxColum = allNumbers.get(i).size();
            }
        }
        
        for (int j = 0; j < maxColum; j++) {
            int max = 0;
            boolean flag = true;
            for (int i = 0; i < allNumbers.size(); i++) {
                if (allNumbers.get(i).size() > j) {
                    if (flag) {
                        max = allNumbers.get(i).get(j);
                        flag = false;
                    } else if (max < allNumbers.get(i).get(j)) {
                        max = allNumbers.get(i).get(j);
                    }
                }
            }
            for (int i = 0; i < allNumbers.size(); i++) {
                if (allNumbers.get(i).size() > j) {
                    if (max > maxs.get(i).get(j)) {
                        maxs.get(i).set(j, max);
                    }
                }
            }
        }

        for (int i = 0; i < maxs.size(); i++) {
            for (int j = 0; j < maxs.get(i).size(); j++) {
                System.out.print(maxs.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
