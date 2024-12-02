/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

import javax.swing.JLabel;

/**
 *
 * @author eobregon
 */
public class CustomMessages {

    public static void showTemporalLabelMessage(JLabel label, int timeSeconds, String messageToShow) {
        Runnable message = new Runnable() {
            @Override
            public void run() {
                label.setText(messageToShow);
                label.setVisible(true);
                try {
                    Thread.sleep(timeSeconds);
                    label.setVisible(false);
                } catch (Exception e) {
                }
            }
        };
        Thread t = new Thread(message);
        t.start();

    }

    public static void showTemporalProgessBar(javax.swing.JProgressBar jpbLoading, int timeSeconds) {
        Runnable message = new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(timeSeconds);
                    jpbLoading.setVisible(false);
                } catch (Exception e) {
                }
            }
        };
        Thread t = new Thread(message);
        t.start();

    }
}
