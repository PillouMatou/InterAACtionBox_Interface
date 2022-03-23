package main.process;

import lombok.extern.slf4j.Slf4j;

import main.process.xdotoolProcess.SendMailXdotoolProcessCreator;

@Slf4j
public class SendMailNamedProcessCreator {

    public void start(String firstname, String lastname, String email, String object, String text, String password) {
        SendMailXdotoolProcessCreator SendMailXdotoolProcessCreator = new SendMailXdotoolProcessCreator(firstname, lastname, email, object, text, password);
        SendMailXdotoolProcessCreator.start();
    }
}
