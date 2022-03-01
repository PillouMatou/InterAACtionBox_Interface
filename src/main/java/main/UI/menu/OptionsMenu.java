package main.UI.menu;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import main.ConfigurationBuilder;
import main.Main;
import main.UI.I18NLabel;
import main.UI.Translator;
import main.process.GnomeControlCenterNamedProcessCreator;
import main.process.TeamviewerNamedProcessCreator;
import main.utils.StageUtils;
import main.utils.UtilsUI;

import java.io.IOException;

@Slf4j
public class OptionsMenu extends BorderPane {

    public String langage = "Francais";

    //private final Translator translator;


    public OptionsMenu(GraphicalMenus graphicalMenus, Main main, Configuration configuration) {
        super();

        Translator translator = main.getTranslator();

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        this.setTop(UtilsUI.createTopBar(translator,graphicalMenus.getHomeScreen(), graphicalMenus, "Options"));

        GridPane settings = new GridPane();
        settings.setHgap(20);
        settings.setVgap(graphicalMenus.primaryStage.getHeight() / 30);

        {
            Label useEyeTracker = new Label("Eye Tracker:");
            useEyeTracker.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            CheckBox useEyeTrackerCheckBox = new CheckBox("Activ\u00e9");
            String style = "-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 2.5em; ";
            useEyeTrackerCheckBox.setStyle(style);
            useEyeTrackerCheckBox.hoverProperty().addListener((obs, oldval, newval) -> {
                if (newval) {
                    useEyeTrackerCheckBox.setStyle(style + "-fx-cursor: hand; -fx-underline: true");
                } else {
                    useEyeTrackerCheckBox.setStyle(style);
                }
            });
            useEyeTrackerCheckBox.selectedProperty().addListener((obj, oldval, newval) -> {
                if (newval) {
                    graphicalMenus.getConfiguration().setMode(Configuration.GAZE_INTERACTION);
                } else {
                    graphicalMenus.getConfiguration().setMode(Configuration.MOUSE_INTERACTION);
                }
            });

            useEyeTrackerCheckBox.setSelected(false);
            useEyeTrackerCheckBox.setTextFill(Color.web("#faeaed"));
            useEyeTrackerCheckBox.resize(100, 100);

            settings.add(useEyeTracker, 0, 1);
            settings.add(useEyeTrackerCheckBox, 1, 1);
        }

        createGnomeControlCenterButtonI18N(translator, graphicalMenus, settings, "Gestionnaire Wifi:", "images/wi-fi_white.png", "wifi", 2);
        createGnomeControlCenterButtonI18N(translator, graphicalMenus, settings, "Gestionnaire Bluetooth:", "images/bluetooth.png", "bluetooth", 3);
        createGnomeControlCenterButtonI18N(translator, graphicalMenus, settings, "Param\u00e8tres D'Affichage:", "images/notebook.png", "display", 4);
        createGnomeControlCenterButtonI18N(translator, graphicalMenus, settings, "Param\u00e8tres de Batterie:", "images/battery.png", "power", 5);
        createGnomeControlCenterButtonLang(translator, settings, configuration);

        {

            Label userInformationLabel = new I18NLabel(translator,"Une id\u00e9e ? Besoin d'aide ? ");
            userInformationLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            Button userInformationButton = UtilsUI.createI18NButton(translator,
                    "Contactez-nous>",
                    "images/contact.png",
                    (e) -> {
                        StageUtils.killRunningProcess(graphicalMenus);
                        graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getContactUs());
                    }
            );

            userInformationButton.setTextFill(Color.web("#faeaed"));

            settings.add(userInformationLabel, 0, 8);
            settings.add(userInformationButton, 1, 8);
        }

        {
            Label changePasswordLabel = new I18NLabel(translator,"Mot de Passe");
            changePasswordLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #cd2653; -fx-font-size: 3em");


            Button changePasswordButton = UtilsUI.createI18NButton(translator,
                    "Changer>",
                    "images/user_white.png",
                    (e) -> {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("bash", "./scripts/changePassword.sh");
                            pb.inheritIO().start();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
            );

            changePasswordButton.setTextFill(Color.web("#faeaed"));

            settings.add(changePasswordLabel, 0, 7);
            settings.add(changePasswordButton, 1, 7);
        }

        {

            Label teamviewerLabel = new I18NLabel(translator,"Lancer TeamViewer:");
            teamviewerLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            Button teamViewerButton = UtilsUI.createI18NButton(translator,
                    "Ouvrir>",
                    "images/teamviewer.png",
                    (e) -> {
                        StageUtils.killRunningProcess(graphicalMenus);
                        TeamviewerNamedProcessCreator teamviewerNamedProcessCreator = new TeamviewerNamedProcessCreator();
                        teamviewerNamedProcessCreator.setUpProcessBuilder();
                        graphicalMenus.process = teamviewerNamedProcessCreator.start(graphicalMenus);
                    }
            );

            teamViewerButton.setTextFill(Color.web("#faeaed"));

            settings.add(teamviewerLabel, 0, 6);
            settings.add(teamViewerButton, 1, 6);
        }

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings, Pos.CENTER);
        this.setCenter(settings);

        this.setBottom(new Label(Configuration.VERSION));
    }

    void createGnomeControlCenterButtonI18N(Translator translator,GraphicalMenus graphicalMenus, GridPane settings, String label, String imageName, String panelToOpen, int row) {
        Label displayedLabel = new I18NLabel(translator,label);
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #cd2653; -fx-font-size: 3em");

        Button button = UtilsUI.createI18NButton(translator,
                "Ouvrir>",
                imageName,
                (e) -> {
                    StageUtils.killRunningProcess(graphicalMenus);
                    GnomeControlCenterNamedProcessCreator process = new GnomeControlCenterNamedProcessCreator(panelToOpen);
                    process.setUpProcessBuilder();
                    graphicalMenus.process = process.start(graphicalMenus);
                }
        );

        button.setTextFill(Color.web("#faeaed"));

        settings.add(displayedLabel, 0, row);
        settings.add(button, 1, row);
    }

    void createGnomeControlCenterButtonLang(Translator translator, GridPane settings, Configuration configuration) {
        Label displayedLabel = new I18NLabel(translator,"Choisir une langue:");
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");
        displayedLabel.setTextFill(Color.web("#cd2653"));

        MenuItem menuItemFR = new MenuItem("Francais");
        MenuItem menuItemEN = new MenuItem("English");

        MenuButton menuButton = new MenuButton(langage);

        menuItemFR.setOnAction(eventMenuLanguages -> {
            configuration.setLanguage("fra");
            ConfigurationBuilder.createFromPropertiesResource().withLanguage(configuration.getLanguage()).saveConfigIgnoringExceptions();
            langage = menuItemFR.getText();
            menuButton.setText(langage);
            translator.changeLanguage(configuration.getLanguage());
            translator.notifyLanguageChanged();
        });
        menuItemEN.setOnAction(eventMenuLanguages -> {
            configuration.setLanguage("eng");
            ConfigurationBuilder.createFromPropertiesResource().withLanguage(configuration.getLanguage()).saveConfigIgnoringExceptions();
            langage = menuItemEN.getText();
            menuButton.setText(langage);
            translator.changeLanguage(configuration.getLanguage());
            translator.notifyLanguageChanged();
        });
        menuButton.getItems().addAll(menuItemEN,menuItemFR);

        settings.add(displayedLabel, 0, 0);
        settings.add(menuButton,1, 0);
    }


}
