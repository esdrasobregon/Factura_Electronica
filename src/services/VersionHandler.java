/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author eobregon
 */
public class VersionHandler {

    public boolean updateVersion() {
        boolean res = false;
        File srcFolder = new File(logic.AppStaticValues.serverAppPath);
        File destFolder = new File(logic.AppStaticValues.productionAppPath);
        try {
            boolean flag = FilesFunctions.checkCreateDirectory(logic.AppStaticValues.productionAppPath);
            if (flag) {
                FileUtils.copyDirectory(srcFolder, destFolder);
                System.out.println("Folder and its contents downloaded successfully!");
                //runOtherJarApp(destFolder + "\\" + logic.AppStaticValues.appName);
                res = true;
            } else {
                JOptionPane.showMessageDialog(null, "El directorio " + logic.AppStaticValues.productionAppPath + " no existe");
            }
        } catch (Exception e) {
            System.err.println("Error downloading folder: " + e.getMessage());
        }
        return res;
    }

    public String getCurrentAppVersion() {
        String v = "";
        try {
            File inputFile = new File(logic.AppStaticValues.serverAppPath
                    + "\\" + logic.AppStaticValues.productionVersionFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("version");
            Node node = nodeList.item(0);
            Element element = (Element) node;
            v = element.getTextContent();
            System.out.println("version " + v);

        } catch (Exception e) {
            System.out.println("data.test.getCurrentAppVersion() error " + e.getMessage());
        }
        return v;
    }

    public void runOtherJarApp(String location) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", location);

            Process process = processBuilder.start();
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createXMLVersionFile() {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<version>0.2.5 test</version>";

        try {
            File file = new File("\\\\192.168.0.10\\2 - soporte\\2-PROGRAMAS\\edras\\Factura electronica\\app\\Factura electronica version.xml");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(xmlContent);
            fileWriter.close();
            System.out.println("XML file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void openCmd() {
        try {
            Runtime.getRuntime().exec("cmd /c start cd c://");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
