package util;

import java.util.Random;

public class LivrariaUtil {
    
    public static String gerarRandomIntString(int lenght) {
        String txt = ""; 
        Random r = new Random();
        int[] x = new int[lenght];
        for (int i = 0; i < lenght; i++) {
            x[i] = 1+r.nextInt(9);
            txt += x[i];
        }
        return txt;
    }
    
    
}
