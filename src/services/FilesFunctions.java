/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.File;

/**
 *
 * @author eobregon
 */
public class FilesFunctions {

    public static boolean renameFile(String fileName, String newName) {
        boolean result = false;
        File file = new File(fileName);
        File newFile = new File(newName);

        if (file.renameTo(newFile)) {
            System.out.println("File name changed from " + fileName + " to " + newFile);
            result = true;
        } else {
            System.out.println("Failed to change file name from " + fileName + " to " + newName);
        }
        return result;
    }

    /**
     * this function checks if a given directory exists, if it does not then it
     * is created
     *
     * @param directoryPath is the path to the directory
     */
    public static boolean checkCreateDirectory(String directoryPath) {
        boolean res = false;
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdir();
            System.out.println("Directory created successfully.");
            res = true;
        } else {
            System.out.println("Directory already exists.");
            res = true;
        }
        return res;
    }
}
