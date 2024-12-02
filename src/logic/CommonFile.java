/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.awt.Desktop;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author programador1
 */
public class CommonFile {
    
    
    public void openFile(String filename) {
        String[] options = new String[]{"Abrir archivo", "Ahora no"};
        int option = JOptionPane.showOptionDialog(null, "Quiere abrir el archivo", "Mensaje", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (option == 0) {
            try {
                File file = new File(filename);
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (Exception e) {
                System.out.println("error en logic.SimpleExcelWriter.openFile() " + e.getMessage());
            }
        }
    }
    
}
