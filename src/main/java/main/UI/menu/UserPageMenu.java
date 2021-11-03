package main.UI.menu;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import main.utils.UtilsUI;

import java.io.*;

@Slf4j
public class UserPageMenu extends BorderPane {

    public UserPageMenu(GraphicalMenus graphicalMenus) {
        super();

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        this.setTop(UtilsUI.createTopBar(graphicalMenus.getOptionsMenu(), graphicalMenus, "Informations de l'utilisateur"));

        GridPane settings = new GridPane();
        settings.setHgap(20);
        settings.setVgap(graphicalMenus.primaryStage.getHeight() / 20);

        {
            Label displayedLabel = new Label("Mot de Passe");
            displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #cd2653; -fx-font-size: 3em");

            Button button = UtilsUI.createButton(
                    "Changer>",
                    "images/user_white.png",
                    (e) -> {
                        try {
                            changePassword();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
            );

            button.setTextFill(Color.web("#faeaed"));

            settings.add(displayedLabel, 0, 0);
            settings.add(button, 1, 0);
        }

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings, Pos.CENTER);
        this.setCenter(settings);
    }

    void changePassword() throws IOException {
            ProcessBuilder pb = new ProcessBuilder("sh", "./scripts/changePassword.sh");
            pb.redirectErrorStream(true);
            pb.start();
    }

}
