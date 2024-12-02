/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author eobregon
 */
public class InternetFunctions {

    public static boolean getInternetConnection() {
        boolean res = false;
        try {
            InetAddress inetAddress = InetAddress.getByName("www.google.com");
            res = inetAddress.isReachable(5000); // Timeout in milliseconds
            System.out.println("Internet Connection Status: " + (res ? "Connected" : "Disconnected"));
        } catch (IOException e) {
            System.out.println("Internet Connection Status: Disconnected");
        }
        return res;
    }
    
}
