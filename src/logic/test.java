/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author eobregon
 */
public class test {

    /*public static void main(String[] args) {
        double number = 1234567890.12;
        String formattedNumber = formatNumber(number);
        System.out.println(formattedNumber);
        System.out.println("number "+number);
    }

    public static String formatNumber(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.CHINA);
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###.##", symbols);
        return decimalFormat.format(number);
    }*/
 /*public static void main(String[] args) {
        double number = 12655634567.89;
        Locale locale = new Locale("en", "US");
        //Locale costaRicaLocale = new Locale("es", "CR");
        NumberFormat numberFormat = NumberFormat.getInstance(locale);

        String formattedNumber = numberFormat.format(number);
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###.##");
        System.out.println("Formatted number in Costa Rica style: " + formattedNumber);
        System.out.println("Formatted number in USA style: " + decimalFormat.format(number));
    }*/
    public static void main(String[] args) {
        /*String filePath = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas\\Facturas contado\\Proforma_100_2024-08-23.jpeg";
        File file = new File(filePath);
        String newFilePath = getFileNewIndexedName(file);
        System.out.println("New file path: " + newFilePath);
        saveFile(file, newFilePath);*/
        getfiles();
    }

    public static String getFileNewIndexedName(File file) {
        String fileName = file.getName();
        String fileDir = file.getParent();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        int index = 1;

        while (file.exists()) {
            String newFileName = String.format("%s_%02d%s", baseName, index++, extension);
            file = new File(fileDir, newFileName);
        }

        return file.getPath();
    }

    public static boolean saveFile(File selectedFile, String newName) {
        boolean res = false;
        Path sourcePath = Paths.get(selectedFile.getAbsolutePath());

        try {
            int index = selectedFile.getName().lastIndexOf(".");
            String extension = selectedFile.getName().substring(index, selectedFile.getName().length());
            Path destinationPath = Paths.get(newName);
            Files.copy(sourcePath, destinationPath);
            System.out.println("logic.util.FileHandler.saveFile() File copied successfully!");
            res = true;
        } catch (IOException e) {
            System.out.println("logic.util.FileHandler.saveFile() error " + e.getMessage());
        }
        return res;
    }

    public static void getfiles() {
        String path = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas\\Facturas contado\\"; // Specify your directory path
        String fileName = "Proforma_100_2024-08-23"; // Specify the file name to search for

        File directory = new File(path);
        if (directory.isDirectory()) {
            File[] matchingFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(fileName);
                }
            });

            if (matchingFiles != null) {
                for (File file : matchingFiles) {
                    System.out.println("Found file: " + file.getAbsolutePath());
                }
            } else {
                System.out.println("No files found.");
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }
    }
}
