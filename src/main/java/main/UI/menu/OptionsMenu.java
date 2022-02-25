package main.UI.menu;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import main.UI.DoubleClickedButton;
import main.process.GnomeControlCenterNamedProcessCreator;
import main.process.TeamviewerNamedProcessCreator;
import main.utils.StageUtils;
import main.utils.UtilsUI;
import tobii.Tobii;

import java.io.IOException;

@Slf4j
public class OptionsMenu extends BorderPane {

    public String langage = "Francais";

    public OptionsMenu(GraphicalMenus graphicalMenus) {
        super();

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        this.setTop(UtilsUI.createTopBar(graphicalMenus.getHomeScreen(), graphicalMenus, "Options"));

        GridPane settings = new GridPane();
        settings.setHgap(20);
        settings.setVgap(graphicalMenus.primaryStage.getHeight() / 30);

        {
            Label useEyeTracker = new Label("Eye Tracker:");
            useEyeTracker.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            Label errorEyeTracker = new Label("");
            errorEyeTracker.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 1em ; -fx-text-fill: #cd2653");

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
                    if(testCoordEyeTracker()){
                        graphicalMenus.getConfiguration().setMode(Configuration.MOUSE_INTERACTION);
                        errorEyeTracker.setText("Pas de eye tracker connect\u00e9 ou de premi\u00e8re calibration faite !");
                        useEyeTrackerCheckBox.setSelected(false);
                    }else {
                        errorEyeTracker.setText("");
                    }
                } else {
                    graphicalMenus.getConfiguration().setMode(Configuration.MOUSE_INTERACTION);
                }
            });

            useEyeTrackerCheckBox.setSelected(false);
            useEyeTrackerCheckBox.setTextFill(Color.web("#faeaed"));
            useEyeTrackerCheckBox.resize(100, 100);

            settings.add(useEyeTracker, 0, 1);
            settings.add(useEyeTrackerCheckBox, 1, 1);
            settings.add(errorEyeTracker, 2, 1);
        }

        createGnomeControlCenterButton(graphicalMenus, settings, "Gestionnaire Wifi:", "images/wi-fi_white.png", "wifi", 2);
        createGnomeControlCenterButton(graphicalMenus, settings, "Gestionnaire Bluetooth:", "images/bluetooth.png", "bluetooth", 3);
        createGnomeControlCenterButton(graphicalMenus, settings, "Param\u00e8tres D'Affichage:", "images/notebook.png", "display", 4);
        createGnomeControlCenterButton(graphicalMenus, settings, "Param\u00e8tres de Batterie:", "images/battery.png", "power", 5);
        createGnomeControlCenterButtonLang(settings);

        {

            Label userInformationLabel = new Label("Une id\u00e9e ? Besoin d'aide ? ");
            userInformationLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            Button userInformationButton = UtilsUI.createButton(
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
            Label changePasswordLabel = new Label("Mot de Passe");
            changePasswordLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #cd2653; -fx-font-size: 3em");


            Button changePasswordButton = UtilsUI.createButton(
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

            Label teamviewerLabel = new Label("Lancer TeamViewer:");
            teamviewerLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            Button teamViewerButton = UtilsUI.createButton(
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

    void createGnomeControlCenterButton(GraphicalMenus graphicalMenus, GridPane settings, String label, String imageName, String panelToOpen, int row) {
        Label displayedLabel = new Label(label);
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #cd2653; -fx-font-size: 3em");

        Button button = UtilsUI.createButton(
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

    void createGnomeControlCenterButtonLang(GridPane settings) {
        Label displayedLabel = new Label("Choisir une langue:");
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");
        displayedLabel.setTextFill(Color.web("#cd2653"));

        MenuItem menuItemFR = new MenuItem("Francais");
        MenuItem menuItemEN = new MenuItem("English");

        MenuButton menuButton = new MenuButton(langage);

        menuItemFR.setOnAction(eventMenuLanguages -> {
            langage = menuItemFR.getText();
            menuButton.setText(langage);
        });
        menuItemEN.setOnAction(eventMenuLanguages -> {
            langage = menuItemEN.getText();
            menuButton.setText(langage);
        });
        menuButton.getItems().addAll(menuItemEN,menuItemFR);

        settings.add(displayedLabel, 0, 0);
        settings.add(menuButton,1, 0);
    }

    public boolean testCoordEyeTracker(){
        final float[] pointAsFloatArray = Tobii.gazePosition();

        final float xRatio = pointAsFloatArray[0];
        final float yRatio = pointAsFloatArray[1];

        return xRatio == 0.5 && yRatio == 0.5;
    }
}
