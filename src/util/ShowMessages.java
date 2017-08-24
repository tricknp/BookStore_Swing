package util;

import javax.swing.JOptionPane;

public class ShowMessages {
    
    private static JOptionPane jop = new JOptionPane();
    
    public static void showMessage(String mensagem) {
        jop.showMessageDialog(null, mensagem);
    }
    
    public static int showQuestion(String txt) {
        return jop.showConfirmDialog(null, txt);
    } 
    
    public static String showInputString(String txt) {
        return jop.showInputDialog(null, txt, "Responda", JOptionPane.QUESTION_MESSAGE);
    } 
    
}
