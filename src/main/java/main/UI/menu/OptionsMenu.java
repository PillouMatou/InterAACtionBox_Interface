package main.UI.menu;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import main.Configuration;
import main.UI.DoubleClickedButton;
import main.process.GnomeControlCenterNamedProcessCreator;
import main.process.TeamviewerNamedProcessCreator;
import main.utils.StageUtils;
import main.utils.UtilsUI;

public class OptionsMenu extends BorderPane {

    public OptionsMenu(GraphicalMenus graphicalMenus) {
        super();

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        this.setTop(UtilsUI.createTopBar(graphicalMenus.getHomeScreen(), graphicalMenus, "Options"));

        GridPane settings = new GridPane();
        settings.setHgap(15);
        settings.setVgap(graphicalMenus.primaryStage.getHeight() / 20);

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
                    useEyeTrackerCheckBox.setText("D\u00e9sactiv\u00e9");
                    graphicalMenus.getConfiguration().setMode(Configuration.MOUSE_INTERACTION);
                } else {
                    useEyeTrackerCheckBox.setText("Activ\u00e9");
                    graphicalMenus.getConfiguration().setMode(Configuration.GAZE_INTERACTION);
                }
            });

            useEyeTrackerCheckBox.setSelected(true);
            useEyeTrackerCheckBox.setTextFill(Color.web("#faeaed"));
            useEyeTrackerCheckBox.resize(100, 100);

            settings.add(useEyeTracker, 0, 0);
            settings.add(useEyeTrackerCheckBox, 1, 0);
        }

        createGnomeControlCenterButton(graphicalMenus, settings, "Gestionnaire Wifi:", "images/wi-fi_white.png", "wifi", 1);
        createGnomeControlCenterButton(graphicalMenus, settings, "Gestionnaire Bluetooth:", "images/bluetooth.png", "bluetooth", 2);
        createGnomeControlCenterButton(graphicalMenus, settings, "Param\u00e8tres D'Affichage:", "images/notebook.png", "display", 3);
        createGnomeControlCenterButton(graphicalMenus, settings, "Param\u00e8tres de Batterie:", "images/battery.png", "power", 4);

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

            settings.add(userInformationLabel, 0, 7);
            settings.add(userInformationButton, 1, 7);
        }

        {

            Label userInformationLabel = new Label("Informations de l'utilisateur:");
            userInformationLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 3em ; -fx-text-fill: #cd2653");

            Button userInformationButton = UtilsUI.createButton(
                    "Ouvrir>",
                    "images/user_white.png",
                    (e) -> {
                        StageUtils.killRunningProcess(graphicalMenus);
                        graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getUserPageMenu());
                    }
            );

            userInformationButton.setTextFill(Color.web("#faeaed"));

            settings.add(userInformationLabel, 0, 6);
            settings.add(userInformationButton, 1, 6);
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

            settings.add(teamviewerLabel, 0, 5);
            settings.add(teamViewerButton, 1, 5);
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

}
