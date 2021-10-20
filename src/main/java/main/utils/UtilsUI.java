package main.utils;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
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
        ImageView graphic = new ImageView(imagePath);
        graphic.setPreserveRatio(true);
        graphic.setFitHeight((primaryStage.getHeight() / 10) * 0.7);
        optionButton.setGraphic(graphic);
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
