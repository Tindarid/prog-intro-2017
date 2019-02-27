public class SumDoubleSpace {
    public static void main(String [] args) {       
        double ans = 0;
        for (String s : args) {
            int i = -1;
            for (int j = 0; j < s.length(); ++j) {
                char smth = s.charAt(j);
                if (Character.isDigit(smth) || smth == '.' || smth == 'e' || smth == '-' || smth == '+' || smth == 'E') {
                    if (i == -1) {
                        i = j;
                    }
                } else if (i != -1) {
                    ans += Double.parseDouble(s.substring(i, j));
                    i = -1;
                }
            }
            if (i != -1) {
                ans += Double.parseDouble(s.substring(i));
            }
        }
        System.out.println(ans);        
    }
}
