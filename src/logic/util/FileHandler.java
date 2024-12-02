/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author eobregon
 */
public class FileHandler extends JFrame {

    /**
     * this method permits the user to choose a directory from the system
     */
    public String getDirectory() {
        String file = "";
        JFileChooser fchoose = new JFileChooser();
        fchoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fchoose.setBackground(new java.awt.Color(51, 51, 51));
        int option = fchoose.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            file = path + "\\" + name;
            //file = path + "\\" + name + ".xls";
            //logic.util.JTableToExcel.exporJTableToExcel(this.jtbCargaTotal, new File(file));
            System.out.println("location " + file);
        }
        return file;
    }

    public String selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a File");
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    public boolean saveFile(File selectedFile, String newName) {
        boolean res = false;
        Path sourcePath = Paths.get(selectedFile.getAbsolutePath());

        try {
            int index = selectedFile.getName().lastIndexOf(".");
            String extension = selectedFile.getName().substring(index, selectedFile.getName().length());
            Path destinationPath = Paths.get(logic.AppStaticValues.archivosFacturasContado + newName + extension);
            Files.copy(sourcePath, destinationPath);
            System.out.println("logic.util.FileHandler.saveFile() File copied successfully!");
            res = true;
        } catch (IOException e) {
            System.out.println("logic.util.FileHandler.saveFile() error " + e.getMessage());
        }
        return res;
    }

    public String getFileNewIndexedName(File file) {
        String fileName = file.getName();
        String fileDir = file.getParent();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        int index = 1;

        while (file.exists()) {
            String newFileName = String.format("%s_%02d%s", baseName, index++, extension);
            file = new File(fileDir, newFileName);
        }

        return file.getName().replace(extension, "");
    }

    public void openFilesBySerchinString(String searchString, String path) {
        File directory = new File(path);

        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(searchString);
            }
        });

        if (files != null && files.length > 0) {
            // Open the first file found
            File fileToOpen = files[0];
            // Add your file opening logic here
            System.out.println("Opening file: " + fileToOpen.getName());
            try {
                Desktop.getDesktop().open(fileToOpen);
            } catch (Exception e) {
                System.err.println("logic.util.FileHandler.openFiles() error " + e.getMessage());
            }
        } else {
            System.err.println("logic.util.FileHandler.openFiles() documento " + searchString);
            JOptionPane.showMessageDialog(null, "No se han encontrado archivos para este documento.");
        }
    }

    public boolean getFilteredfiles(String path, String fileName) {
        boolean res = false;
        File directory = new File(path);
        if (directory.isDirectory()) {
            File[] matchingFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(fileName);
                }
            });

            if (matchingFiles != null || matchingFiles.length > 0) {
                //JOptionPane.showMessageDialog(null, "se han encontrado "+matchingFiles.length+" archivos");
                for (File file : matchingFiles) {
                    try {
                        res = true;
                        Desktop.getDesktop().open(file);
                    } catch (Exception e) {
                        System.err.println("logic.util.FileHandler.getfiles() error " + e.getMessage());
                    }
                    System.out.println("Found file: " + file.getAbsolutePath());
                }
            } else {
                System.out.println("No files found.");
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }
        return res;
    }

    public int countMatchingfiles(String path, String fileName) {
        int res = 0;
        File directory = new File(path);
        if (directory.isDirectory()) {
            File[] matchingFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(fileName);
                }
            });

            if (matchingFiles != null || matchingFiles.length > 0) {
                res = matchingFiles.length;
            } else {
                System.out.println("No files found.");
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }
        return res;
    }

    public static boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

}
