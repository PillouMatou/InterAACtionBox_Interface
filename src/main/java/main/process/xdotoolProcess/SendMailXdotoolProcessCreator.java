package main.process.xdotoolProcess;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SendMailXdotoolProcessCreator {

    ProcessBuilder processBuilder;

    public SendMailXdotoolProcessCreator(String firstname, String lastname, String email, String object, String text){
        setUpProcessBuilder(firstname, lastname, email, object, text);
    }

    public void setUpProcessBuilder(String firstname, String lastname, String email, String object, String text) {
        processBuilder = new ProcessBuilder(
                "sh",
                "./scripts/sendMail.sh",
                firstname,
                lastname,
                text,
                object,
                email
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
