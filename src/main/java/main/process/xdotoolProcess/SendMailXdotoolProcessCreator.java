package main.process.xdotoolProcess;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SendMailXdotoolProcessCreator {

    ProcessBuilder processBuilder;

    public SendMailXdotoolProcessCreator(String firstname, String lastname, String email, String object, String text, String password){
        setUpProcessBuilder(firstname, lastname, email, object, text, password);
    }

    public void setUpProcessBuilder(String firstname, String lastname, String email, String object, String text, String password) {
        processBuilder = new ProcessBuilder(
                "sh",
                "./scripts/sendMail.sh",
                firstname,
                lastname,
                text,
                object,
                email,
                password
        );
    }

    public Process start() {
        try {
            return processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
