/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.email;

/**
 *
 * @author eobregon
 */
import static data.CrudFacturaElectronica.addFacturaElectronicaRepetida;
import data.DataUser;
import entitys.Receips;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.*;
import java.io.*;
import services.FileHandler;

public class EmailFileReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataUser.username = "edras";
        DataUser.password = "Obr5612on";
        // TODO code application logic here
        String host = "pop.ciltcr.com";
        String mailStoreType = "pop3";
        String user = "facturaciongeneral@ciltcr.com";
        String password = "q-sG=)wmv9)V";
        Properties prop = new Properties();
        prop.put("mail.pop3.host", host);
        prop.put("mail.pop3.port", "995");
        prop.put("mail.pop3.starttls.enable", "true");
        check(prop, host, mailStoreType, user, password);
    }

    public static void check(Properties prop, String host, String storeType, String user, String password) {
        try {
            Session emailSession = Session.getDefaultInstance(prop);

            Store store = emailSession.getStore("pop3s");
            store.connect(host, user, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message messages[] = emailFolder.getMessages();

            loopMessages(messages);

            emailFolder.close(true);
        } catch (NoSuchProviderException e) {
            System.out.println("emailtest.EmailTest.check() error " + e.getMessage());
        } catch (MessagingException e) {
            System.out.println("emailtest.EmailTest.check() error " + e.getMessage());

        }

    }

    private static void loopMessages(Message messages[]) {
        int emailIndex = data.CrudFacturaElectronica.getEmailMaxIndex();
        for (int i = emailIndex + 1; i < messages.length; i++) {
            try {
                Message message = messages[i];
                System.out.println("email number " + (i));
                System.out.println("email subject " + message.getSubject());
                System.out.println("email from " + message.getFrom());
                //System.out.println("Email attachments " + message.get);
                //readAttachments(message);
                saveAttachmentFile(message, i);
            } catch (MessagingException e) {
                System.out.println("emailtest.EmailTest.loopMessages() error " + e.getMessage() + " message number " + i);
            }

        }

    }

    private static void readAttachments(Message message) {
        try {
            if (message.isMimeType("multipart/*")) {
                MimeMultipart multipart = (MimeMultipart) message.getContent();
                int count = multipart.getCount();
                for (int j = 0; j < count; j++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(j);
                    // Check if the body part is an attachment and its content type is XML
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
                            && bodyPart.getContentType().contains("xml")) {

                        // Get the input stream of the attachment
                        InputStream inputStream = bodyPart.getInputStream();

                        // Create a byte array to read the attachment content
                        byte[] attachmentBytes = new byte[inputStream.available()];

                        // Read the attachment content into the byte array
                        inputStream.read(attachmentBytes);

                        // Convert the byte array to a string
                        String attachmentContent = new String(attachmentBytes);

                        // Print the attachment content
                        System.out.println("Attachment Content:");
                        System.out.println(attachmentContent);

                        // Close the input stream
                        inputStream.close();

                    }
                }
            }
        } catch (IOException e) {
            System.out.println("emailtest.EmailTest.check() error " + e.getMessage());
        } catch (MessagingException e) {
            System.out.println("emailtest.EmailTest.check() error " + e.getMessage());
        }

    }

    public static void saveAttachmentFile(Message message, int emailIndex) {
        //String saveDirectory = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas\\guardados\\";//\\\\192.168.0.10\\2 - soporte\\2-PROGRAMAS\\edras\\backup facturas\\";
        try {
            if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();
                int numberOfParts = multipart.getCount();

                // Iterate over the parts of the message
                for (int partIndex = 0; partIndex < numberOfParts; partIndex++) {
                    MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partIndex);
                    save(part, emailIndex);
                }
            }
        } catch (MessagingException e) {
            System.out.println("emailtest.EmailTest.saveAttachmentFile() error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("emailtest.EmailTest.saveAttachmentFile() error " + e.getMessage());
        }
    }

    private static void save(MimeBodyPart part, int emailIndex) {// Check if the part is an XML attachment

        try {
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String fileName = part.getFileName();
                String extension = fileName.substring(fileName.length() - 4, fileName.length());
                // Save the XML attachment to the specified directory
                System.err.println("extension " + extension);
                if (extension.equalsIgnoreCase(".xml") || extension.equalsIgnoreCase(".pdf")) {
                    File file = new File(logic.AppStaticValues.respaldoArchivosGuardados + File.separator + fileName);
                    part.saveFile(file);
                    if (extension.equalsIgnoreCase(".xml")) {
                        Receips rec = FileHandler.getJsonStringFromFile(logic.AppStaticValues.respaldoArchivosGuardados + File.separator + fileName);
                        if (rec != null) {
                            rec.setExactus("");
                            rec.setNombreArchivo(fileName);
                            rec.setEmailIndex(emailIndex);
                            rec.setCuentaGeneral("");
                            rec.setCuentaPresupuesto("");

                            boolean res = data.CrudFacturaElectronica.addUpdateFacturaElectronica(rec);
                            if (!res) {
                                data.CrudFacturaElectronica.addFacturaElectronicaRepetida(rec);
                                file = new File(logic.AppStaticValues.respaldoArchivosOmitidos + File.separator + fileName);
                                part.saveFile(file);
                            }
                        }
                    }
                }

            }
        } catch (MessagingException e) {
            System.out.println("services.email.EmailFileReader.save() error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("services.email.EmailFileReader.save() error " + e.getMessage());
        }
    }

    private static void guardarFactura(String nombreArchivo, String filePath, int emailIndex) {

    }
}
