package io.github.thred.climatetray;

import javax.swing.JOptionPane;

public class ClimateTrayUtils
{

    
    public static void infoDialog(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void errorDialog(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

}
