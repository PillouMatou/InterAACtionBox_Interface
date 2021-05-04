package main.UI;

import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class DoubleClickedButton extends Button {

    public DoubleClickedButton(String s){
        super(s);
    }

    public void assignHandler(EventHandler eventhandler)
    {
        this.setOnAction((e)->{
            eventhandler.handle(null);});
    }
}
