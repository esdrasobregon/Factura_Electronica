/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 *
 * @author eobregon
 */
public class SOAPTest extends JFrame {
    private static final String SOAP_ENDPOINT = "http://localhost:8080/GestionPlanillasWebService/test?wsdl";
    private static final String SOAP_ACTION = "	http://localhost:8080/GestionPlanillasWebService/test?wsdl";
    private static final String SOAP_NAMESPACE = "http://localhost:8080";
    
    private JTextArea responseTextArea;
    
    public SOAPTest() {
        setTitle("SOAP Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        
        responseTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(responseTextArea);
        getContentPane().add(scrollPane);
        
        JButton sendButton = new JButton("Send Request");
        sendButton.addActionListener(e -> sendSOAPRequest());
        getContentPane().add(sendButton, BorderLayout.SOUTH);
    }
    
    private void sendSOAPRequest() {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            soapEnvelope.addNamespaceDeclaration("ns", SOAP_NAMESPACE);
            
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPElement soapElement = soapBody.addChildElement("SomeRequest", "ns");
            soapElement.addChildElement("a").setTextContent("12");
            soapElement.addChildElement("b").setTextContent("10");
            
            soapMessage.saveChanges();
            
            SOAPMessage soapResponse = soapConnection.call(soapMessage, SOAP_ENDPOINT);
            
            responseTextArea.setText(soapResponse.getSOAPBody().getTextContent());
            
            soapConnection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SOAPTest soapClient = new SOAPTest();
            soapClient.setVisible(true);
        });
    }
}