public class Sum {
    public static void main(String [] args) {       
        int ans = 0;
        for (String s : args) {
            int i = -1;
            for (int j = 0; j < s.length(); ++j) {
                char smth = s.charAt(j);
                if (!Character.isWhitespace(smth)) {
                    if (i == -1) {
                        i = j;
                    }
                } else if (i != -1) {
                    ans += Integer.parseInt(s.substring(i, j));
                    i = -1;
                }
            }
            if (i != -1) {
                ans += Integer.parseInt(s.substring(i));
            }
        }
        System.out.println(ans);        
    }
}
