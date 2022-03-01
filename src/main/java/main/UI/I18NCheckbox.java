package main.UI;

import javafx.event.EventHandler;
import javafx.scene.control.*;

public class I18NCheckbox extends CheckBox implements Translator.LanguageChangeListener {

    private String textKeys;

    private final Translator translator;


    public I18NCheckbox(Translator translator, String textKeys) {
        super();
        this.textKeys = textKeys;
        this.translator = translator;
        //
        languageChanged();
        //
        translator.registerLanguageChangeListener(this);
    }

    @Override
    public void languageChanged() {
        if (textKeys != null) {
            this.setText(translator.translate(textKeys));
        }
    }

    public void assignHandler(EventHandler eventhandler) {
        this.setOnAction((e) -> {
            eventhandler.handle(null);
        });
    }
}
