package main.utils;

import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Properties;

@Slf4j
public class UtilsMail {
    static Cipher cipher;

    public static String letMeSendIt() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // block size is 128bits
            LinkedList<Integer> byteList = new LinkedList<>();
            BufferedReader buffReader = new BufferedReader(new FileReader("~/.email/crypted_key.txt"));
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

            BufferedReader buffReaderpass = new BufferedReader(new FileReader("~/.email/crypted_pass.txt"));
            return decrypt(buffReaderpass.readLine(), secretKey);
        } catch (Exception e) {
            return "";
        }
    }

    public static void send(Label errorLabel, String firstname, String lastname, String email, String object, String text) {
        if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty()) {
            errorLabel.setText("Entrez votre nom et pr\u00e8nom");
        } else if (email == null || email.isEmpty()) {
            errorLabel.setText("Entrez une addresse email pour que nous puissions vous répondre");
        } else if (object == null || object.isEmpty() || text == null || text.isEmpty()) {
            errorLabel.setText("Renseignez l'objet de votre demande et donnez nous les détails dans votre message");
        } else {
            try {
                sendClient(errorLabel, firstname, lastname, email, object, text);
                sendSupport(firstname, lastname, email, object, text);
                errorLabel.setText("Message envoy\u00e8 !");
            } catch (AuthenticationFailedException e) {
                errorLabel.setText("Service temporairement indisponible");
            } catch (AddressException e) {
                errorLabel.setText("Adresse Email invalide");
            }
        }
    }

    public static void sendSupport(String firstname, String lastname, String email, String object, String text) throws AuthenticationFailedException {
        //Propriétés
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");
        //Session
        Session s = Session.getDefaultInstance(p,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return passWordAuthentification();
                    }
                });
        //composer le message
        try {
            MimeMessage m = new MimeMessage(s);
            m.addRecipient(Message.RecipientType.TO, new InternetAddress("contact.interaactionbox@gmail.com"));
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
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");
        //Session
        Session s = Session.getDefaultInstance(p,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return passWordAuthentification();
                    }
                });
        //composer le message
        MimeMessage m = new MimeMessage(s);
        InternetAddress internetAddress = new InternetAddress(email);
        try {
            m.addRecipient(Message.RecipientType.TO, internetAddress);
            m.setSubject("[Support InterAACtionBox] objet: " + object);
            m.setText("Bonjour " + lastname + " " + firstname +
                    ",\nmerci d'avoir contact\u00e9 le support de l'InterAACtionBox.\nVotre message :\n\n\"" + text + "\"\n\na \u00e9t\u00e9 transmis \u00e0 notre \u00e9quipe qui vous r\u00e9pondra prochainement \u00e0 l'adresse: " + email + "\n\n \u00C0 tr\u00e8s vite !");
            //envoyer le message

            Transport.send(m);
        } catch (AuthenticationFailedException e) {
            throw new AuthenticationFailedException();
        } catch (MessagingException e) {
            errorLabel.setText("Erreur lors de l'envois du message...");
        }
    }

    private static PasswordAuthentication passWordAuthentification() {
        return new PasswordAuthentication("help.interaactionbox@gmail.com", letMeSendIt());
    }

    public static String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }
}
