package main.utils;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.UI.DoubleClickedButton;
import main.UI.ProgressButton;
import main.UI.menu.GraphicalMenus;

import java.awt.image.BufferedImage;

public class UtilsUI {

    public static Button getDoubleClickedButton(String text, String imagePath, EventHandler eventhandler, Stage primaryStage) {
        DoubleClickedButton optionButton = new DoubleClickedButton(text);
        // optionButton.setMaxHeight(50);
        optionButton.setStyle(
                "-fx-border-color: transparent; " +
                        "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-background-color: transparent; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-text-fill: #faeaed;" +
                        "-fx-font-size: 20;"
        );
        if (imagePath != null) {
            ImageView graphic = new ImageView(imagePath);
            graphic.setPreserveRatio(true);
            graphic.setFitHeight((primaryStage.getHeight() / 10) * 0.7);
            optionButton.setGraphic(graphic);
        }
        optionButton.assignHandler(eventhandler);
        return optionButton;
    }

    public static ImageView getDownloadImageView(ProgressButton processButton) {
        Image download = new Image("images/download-arrow.png");
        ImageView downnloadImageView = new ImageView(download);
        downnloadImageView.setPreserveRatio(true);
        downnloadImageView.fitWidthProperty().bind(processButton.getButton().radiusProperty());
        downnloadImageView.fitHeightProperty().bind(processButton.getButton().radiusProperty());
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#cd2653"));
        downnloadImageView.setEffect(dropShadow);
        return downnloadImageView;
    }

    public static StackPane createTopBar(GraphicalMenus graphicalMenus, String label) {
        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0, 0, graphicalMenus.primaryStage.getWidth(), graphicalMenus.primaryStage.getHeight() / 10);
        backgroundForTitle.heightProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(10));
        backgroundForTitle.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundForTitle.setFill(Color.web("#cd2653"));

        Button back = UtilsUI.getDoubleClickedButton("Retour", "images/back.png", (e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getHomeScreen()), graphicalMenus.primaryStage);
        back.prefHeightProperty().bind(backgroundForTitle.heightProperty());

        Label title = new Label(label);
        title.setFont(new Font(30));
        title.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        title.setTextFill(Color.web("#faeaed"));
        BorderPane titleBox = new BorderPane();
        title.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().subtract(back.widthProperty().multiply(2)));
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titleBox.setLeft(back);
        titleBox.setCenter(title);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);
        return titlePane;
    }

    public static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    public static Rectangle createBackground(GraphicalMenus graphicalMenus) {
        Rectangle r = new Rectangle();
        r.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        r.heightProperty().bind(graphicalMenus.primaryStage.heightProperty());
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#faeaed")), new Stop(1, Color.web("#cd2653"))};
        LinearGradient lg1 = new LinearGradient(0, 1, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
        r.setFill(lg1);
        return r;
    }
}
