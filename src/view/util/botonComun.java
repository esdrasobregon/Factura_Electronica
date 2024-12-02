/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

import javax.swing.JButton;

/**
 *
 * @author programador1
 */
public class botonComun {
    public static void mouseHoverListener(JButton jButton1) {
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1.setContentAreaFilled(true);
                jButton1.setBackground(new java.awt.Color(0, 222,183,222));

            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1.setBackground(jButton1.getParent().getBackground());
            }
        });
    }
}