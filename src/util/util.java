package util;

import java.time.LocalDate;

public class util {
    
    public static LocalDate getLocalDate(String string) {
        String split[] = string.split("/");
        int dia = Integer.parseInt(split[0]);
        int mes = Integer.parseInt(split[1]);
        int ano = Integer.parseInt(split[2]);
        LocalDate date = LocalDate.of(ano, mes, dia);
        return date;
    }
    
    public static int getInt(String string) {
        try {
            int x = Integer.parseInt(string);
            return x;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static String convertLocalDateUStoBR(LocalDate ldate) {
        String data = ldate.toString().replaceAll("-", "/");
        String[] s = data.split("/");
        return s[2]+"/"+s[1]+"/"+s[0];
    }
    
}
