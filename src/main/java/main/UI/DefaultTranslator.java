package main.UI;

import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import main.ConfigurationBuilder;
import main.utils.multilinguism.Multilinguism;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class DefaultTranslator implements Translator {

    private final Multilinguism multilinguism;

    private Configuration config;

    private final List<LanguageChangeListener> languageChangeListeners = new CopyOnWriteArrayList<>();

    public DefaultTranslator(Configuration config, Multilinguism multilinguism) {
        this.config = config;
        this.multilinguism = multilinguism;
    }

    @Override
    public String translate(String key) {
        return multilinguism.getTrad(key, config.getLanguage());
    }

    @Override
    public String translate(String... keys) {
        StringBuilder textBuilder = new StringBuilder();
        for (String key : keys) {
            textBuilder.append(translate(key));
        }
        return textBuilder.toString();
    }

    //add to the list the listener (I18N)
    @Override
    public void registerLanguageChangeListener(LanguageChangeListener listener) {
        languageChangeListeners.add(listener);
    }

    //notify all listener to change the language
    @Override
    public void notifyLanguageChanged() {
        // config = ConfigurationBuilder.createFromPropertiesResource().build();
        this.notifyAllListeners();
    }

    //set the config language to the language specified
    @Override
    public void changeLanguage(String language) {
        config.setLanguage(language);
    }

    private void notifyAllListeners() {
        log.info("config defaultTrans {}",config.getLanguage());
        log.info("languageChangeListeners {}",languageChangeListeners);
        for (LanguageChangeListener l : languageChangeListeners) {
            l.languageChanged();
        }
    }

}
