import java.math.BigInteger;

public class SumBigInteger {
    public static void main(String [] args) throws NumberFormatException {       
        BigInteger ans = BigInteger.ZERO;
        for (String s : args) {
            int i = -1;
            for (int j = 0; j < s.length(); ++j) {
                char smth = s.charAt(j);
                if (Character.isDigit(smth) || smth == '-' || smth == '+') {
                    if (i == -1) {
                        i = j;
                    }
                } else if (i != -1) {
                    ans = ans.add(new BigInteger(s.substring(i, j)));
                    i = -1;
                }
            }
            if (i != -1) {
                ans = ans.add(new BigInteger(s.substring(i)));
            }
        }
        System.out.println(ans);        
    }
}
