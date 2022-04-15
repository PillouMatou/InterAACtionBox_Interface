package main.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import main.process.SendMailNamedProcessCreator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Properties;

@Slf4j
public class UtilsMail {
    static Cipher cipher;

    @SuppressFBWarnings
    public static String letMeSendIt() {

        BufferedReader buffReader = null;
        BufferedReader buffReaderpass = null;

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // block size is 128bits
            //LinkedList<Integer> byteList = new LinkedList<>();
            buffReader = new BufferedReader(new FileReader("../../.email/crypted_key.txt", StandardCharsets.UTF_8));
            String[] stringbyteArray = buffReader.readLine().split(",");
            byte[] realBytesArray = new byte[stringbyteArray.length];
            for (int i = 0; i < stringbyteArray.length; i++) {
                realBytesArray[i] = ByteBuffer.allocate(4).putInt(Integer.parseInt(stringbyteArray[i])).array()[3];
            }
            SecretKey secretKey = new SecretKey() {
                @Override
                public String getAlgorithm() {
                    return "AES";
                }

                @Override
                public String getFormat() {
                    return "RAW";
                }

                @Override
                public byte[] getEncoded() {
                    return realBytesArray;
                }
            };
            cipher = Cipher.getInstance("AES");

            buffReaderpass = new BufferedReader(new FileReader("../../.email/crypted_pass.txt", StandardCharsets.UTF_8));
            return decrypt(buffReaderpass.readLine(), secretKey);
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (buffReader != null){
                    buffReader.close();
                }
                if (buffReaderpass != null){
                    buffReaderpass.close();
                }
            }catch (IOException e2){
                e2.printStackTrace();
            }
        }
    }

    public static void send(Label errorLabel, String firstname, String lastname, String email, String object, String text) {
        if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty()) {
            errorLabel.setText("Entrez votre nom et pr\u00e9nom");
        } else if (email == null || email.isEmpty()) {
            errorLabel.setText("Entrez une addresse email pour que nous puissions vous r\u00e9pondre");
        } else if (object == null || object.isEmpty() || text == null || text.isEmpty()) {
            errorLabel.setText("Renseignez l'objet de votre demande et donnez nous les d\u00e9tails dans votre message");
        } else {
            try {
                String password = letMeSendIt();
                SendMailNamedProcessCreator SendMailNamedProcessCreator = new SendMailNamedProcessCreator();
                SendMailNamedProcessCreator.start(firstname, lastname, email, object, text, password);
            } catch (Exception e) {
                errorLabel.setText("Service temporairement indisponible");
            }finally {
                errorLabel.setText("Message envoy\u00e9 !");
            }


            /*try {
                sendClient(errorLabel, firstname, lastname, email, object, text);
                sendSupport(firstname, lastname, email, object, text);
                errorLabel.setText("Message envoy\u00e8 !");
            } catch (AuthenticationFailedException e) {
                errorLabel.setText("Service temporairement indisponible");
            } catch (AddressException e) {
                errorLabel.setText("Adresse Email invalide");
            }*/
        }
    }

    public static void sendSupport(String firstname, String lastname, String email, String object, String text) throws AuthenticationFailedException {
        //Propriétés
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        //Session
        Session s = Session.getInstance(p,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("help.interaactionbox@gmail.com", "l!gug@4B0*");
                    }
                });
        //composer le message
        try {
            Message m = new MimeMessage(s);
            m.setRecipient(Message.RecipientType.TO, new InternetAddress("contact.interaactionbox@gmail.com"));
            m.setSubject("[" + firstname + " " + lastname + "] objet: " + object);
            m.setText(lastname + " " + firstname +
                    " vous a contact\u00e9 avec le message suivant:\n\n\"" + text + "\"\n\n Nom: " + firstname + "\n Pr\u00e9nom: " + lastname + "\n Adresse mail: " + email);
            //envoyer le message
            Transport.send(m);
        } catch (AuthenticationFailedException e) {
            throw new AuthenticationFailedException();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendClient(Label errorLabel, String firstname, String lastname, String email, String object, String text) throws AddressException, AuthenticationFailedException {
        //Propriétés
        Properties p = new Properties();
        p.put("mail.smtp.ssl.trust", "*");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.starttls.enable", "true");
        //Session
        Session s = Session.getDefaultInstance(p,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return passWordAuthentification();
                    }
                });
        //composer le message
        try {
            MimeMessage m = new MimeMessage(s);
            InternetAddress internetAddress = new InternetAddress(email);
            m.setFrom(new InternetAddress("help.interaactionbox@gmail.com"));
            m.setRecipient(Message.RecipientType.TO, internetAddress);
            m.setSubject("[Support InterAACtionBox] objet: " + object);
            m.setText("Bonjour " + lastname + " " + firstname +
                    ",\nmerci d'avoir contact\u00e9 le support de l'InterAACtionBox.\nVotre message :\n\n\"" + text + "\"\n\na \u00e9t\u00e9 transmis \u00e0 notre \u00e9quipe qui vous r\u00e9pondra prochainement \u00e0 l'adresse: " + email + "\n\n \u00C0 tr\u00e8s vite !");
            //envoyer le message

            Transport.send(m, "help.interaactionbox@gmail.com", letMeSendIt());
        } catch (MessagingException e) {
            errorLabel.setText("Erreur lors de l'envois du message...");
        }
    }

    private static PasswordAuthentication passWordAuthentification() {
        return new PasswordAuthentication("help.interaactionbox@gmail.com", letMeSendIt());
    }

    @SuppressFBWarnings
    public static String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }
}
