/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author eobregon
 */
public class CommonPanel {
     public  void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setVisible(isEnabled);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel) component, isEnabled);
            }
            component.setVisible(isEnabled);
        }
    }

    public  void handleResizePanel(JPanel panel, int ancho, int alto) {
        panel.setPreferredSize(new Dimension(ancho, alto));
        panel.invalidate();
        panel.validate();
        panel.repaint();
    }
}
