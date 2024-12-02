/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author eobregon
 */
public class LookAndFeel {

    static int themeindex = 0;
    int index = 0;

    public LookAndFeel() {

    }

    public LookAndFeel(int index) {

        this.index = index;
    }

    public int getIndex() {
        return themeindex;
    }

    public void setIndex() {
        String[] options = new String[]{"FlatDark", "FlatMacDarkLaf", "FlatDarculaLaf", "FlatLightLaf", "Cancel"};
        this.index = JOptionPane.showOptionDialog(null, "Escoge un tema", "Title",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
    }

    public boolean setTheme() {
        boolean res = false;
        try {
            if (this.index != themeindex && this.index > 0) {
                themeindex = index;
                switch (themeindex) {
                    case 0:

                        UIManager.setLookAndFeel(new FlatDarkLaf());
                        System.out.println("selected " + themeindex);

                        break;
                    case 1:
                        UIManager.setLookAndFeel(new FlatMacDarkLaf());
                        System.out.println("selected " + themeindex);

                        break;
                    case 2:

                        UIManager.setLookAndFeel(new FlatDarculaLaf());
                        System.out.println("selected " + themeindex);

                        break;
                    case 3:

                        UIManager.setLookAndFeel(new FlatLightLaf());
                        System.out.println("selected " + themeindex);

                        break;
                    default:
                        getOLdLookAndFeel();
                        break;
                }
            }
            res = true;
        } catch (Exception e) {
            getOLdLookAndFeel();
            System.out.println("view.util.LookAndFeel.setTheme() error " + e.getMessage());
        }
        return res;
    }

    private void getOLdLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }

}
