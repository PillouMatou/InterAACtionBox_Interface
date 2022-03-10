package main.UI.menu;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import main.Configuration;
import main.Main;
import main.UI.I18NLabel;
import main.UI.I18NProgressButton;
import main.UI.I18NText;
import main.UI.Translator;
import main.utils.UtilsOS;

import java.io.IOException;

public class ExitMenu extends BorderPane {

    private GraphicalMenus graphicalMenus;

    public ExitMenu(GraphicalMenus graphicalMenus, Main main, Configuration configuration) {

        Translator translator = main.getTranslator();

        this.graphicalMenus = graphicalMenus;
        Rectangle r = new Rectangle();
        r.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        r.heightProperty().bind(graphicalMenus.primaryStage.heightProperty());
        Stop[] stops = new Stop[]{new Stop(0, Color.WHITE), new Stop(1, Color.BLACK)};
        LinearGradient lg1 = new LinearGradient(0, 1, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
        r.setFill(lg1);

        this.getChildren().add(r);


        StackPane shutdownButton = createAppI18NButtonLauncher(translator,
                (e) -> {
                    if (graphicalMenus.process != null && graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                        graphicalMenus.process.set(null);
                    }
                    try {
                        shutdown();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Platform.exit();
                    System.exit(0);
                },
                "Oui, \u00e9teindre",
                "images/on-off-button.png",
                configuration
        );

        StackPane exitButton = createAppI18NButtonLauncher(translator,
                (e) -> {
                    if (graphicalMenus.process != null && graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                        graphicalMenus.process.set(null);
                    }
                    Platform.exit();
                    System.exit(0);
                },
                "Non, aller vers Ubuntu",
                "images/exit.png",
                configuration
        );

        StackPane cancelButton = createAppI18NButtonLauncher(translator,
                (e) -> {
                    graphicalMenus.primaryStage.getScene().setRoot(graphicalMenus.getHomeScreen());
                },
                "Non, annuler",
                "images/cross.png",
                configuration
        );

        HBox hbox = new HBox(cancelButton, exitButton, shutdownButton);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.spacingProperty().bind(this.widthProperty().divide(20));

        I18NLabel exitPrompt = new I18NLabel(translator,"Voulez vous vraiment \u00e9teindre la box ?");
        exitPrompt.setFont(new Font(60));
        exitPrompt.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        exitPrompt.setTextFill(Color.BLACK);

        VBox vbox = new VBox(exitPrompt, hbox);
        vbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(vbox, Pos.CENTER);
        vbox.spacingProperty().bind(this.heightProperty().divide(10));

        this.setCenter(vbox);

    }

    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;

        if (UtilsOS.isUnix()) {
            shutdownCommand = "shutdown -h now";
        } else if (UtilsOS.isWindows()) {
            shutdownCommand = "shutdown.exe -s -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        Platform.exit();
        System.exit(0);
    }

    private StackPane createAppI18NButtonLauncher(Translator translator, EventHandler eventHandler, String name, String imageURL, Configuration configuration) {

        I18NProgressButton progressButton = new I18NProgressButton();
        progressButton.getButton().setRadius(graphicalMenus.primaryStage.getWidth() / 10);
        progressButton.getButton().setStroke(Color.BLACK);

        ImageView logo = new ImageView(new Image(imageURL));
        logo.setFitWidth(progressButton.getButton().getRadius() * 0.99);
        logo.setFitHeight(progressButton.getButton().getRadius() * 0.99);
        logo.fitWidthProperty().bind(progressButton.getButton().radiusProperty().multiply(0.99));
        logo.fitHeightProperty().bind(progressButton.getButton().radiusProperty().multiply(0.99));
        logo.setPreserveRatio(true);

        progressButton.setImage(logo);

        if(configuration.isGazeInteraction()){
            progressButton.assignIndicator(eventHandler);
        }

        I18NText tradName = new I18NText(translator,name);
        progressButton.getLabel().setText(tradName.getText());
        progressButton.getLabel().setTextFill(Color.BLACK);
        progressButton.getLabel().setFont(new Font(20));
        progressButton.getButton().setStrokeWidth(3);

        progressButton.setOnMouseClicked(eventHandler);

        progressButton.start();
        return progressButton;
    }
}
