package main.UI;

import javafx.event.EventHandler;

public class DoubleClickedI18NButton extends I18NButton{
    public DoubleClickedI18NButton(Translator translator, String... textKeys) {
        super(translator, textKeys);
    }
    public void assignHandler(EventHandler eventhandler) {
        this.setOnAction((e) -> {
            eventhandler.handle(null);
        });
    }
}
