package main;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.LinkedList;

public class MenuPane extends Pane {

    LinkedList<ProgressButton> buttons = new LinkedList<ProgressButton>();

    final ImageView backgroundBlured;
    final Stage primaryStage;
    final Button closeMenuButton;

    public MenuPane(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;
        this.setStyle("-fx-background-color: transparent;");

        backgroundBlured = new ImageView(new Image("images/blured.jpg" ));

        setOpacityBackground(1);

        backgroundBlured.fitWidthProperty().bind(this.primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(this.primaryStage.heightProperty());

        this.getChildren().add(backgroundBlured);
        setBackgroundListener(backgroundBlured);

        closeMenuButton = createCloseMenuButton();
       // this.getChildren().add(closeMenuButton);
    }

    public void setCloseMenuHandler(EventHandler<MouseEvent> eventHandler) {
        closeMenuButton.setOnMouseClicked(eventHandler);
    }

    public Button createCloseMenuButton() {
        Button closeButton = new Button();
        closeButton.setPrefWidth(50);
        closeButton.setPrefHeight(50);

        closeButton.setShape(new Circle(50));

        closeButton.layoutXProperty().bind(primaryStage.widthProperty().divide(2).subtract(25));
        closeButton.layoutYProperty().bind(primaryStage.heightProperty().divide(2).subtract(25));

        ImageView logo = new ImageView(new Image("images/cross.png"));
        logo.setFitWidth(20);
        logo.setFitHeight(20);
        logo.setPreserveRatio(true);
        closeButton.setGraphic(logo);

        return closeButton;
    }

    public void setOpacityBackground(double d) {
        backgroundBlured.setOpacity(d);
    }

    public void setBackgroundListener(ImageView backgroundBlured) {
        backgroundBlured.setOnMouseMoved(event -> {

            for (ProgressButton button : buttons) {
                if (button != null) {
                    double buttonOrigin = Point2D.distance(
                            Screen.getPrimary().getBounds().getWidth() / 2,
                            Screen.getPrimary().getBounds().getHeight() / 2,
                            button.getLayoutX() + button.getPrefWidth() / 2,
                            button.getLayoutY() + button.getPrefHeight() / 2);
                    double mouseOrigin = Point2D.distance(
                            Screen.getPrimary().getBounds().getWidth() / 2,
                            Screen.getPrimary().getBounds().getHeight() / 2,
                            event.getX(),
                            event.getY());
                    double mouseButton = Point2D.distance(
                            button.getLayoutX() + button.getPrefWidth() / 2,
                            button.getLayoutY() + button.getPrefHeight() / 2,
                            event.getX(),
                            event.getY());

                    if (mouseButton < buttonOrigin) {
                        double factor = (mouseOrigin / buttonOrigin) > 1 ? 1 : (mouseOrigin / buttonOrigin);
                        button.setPrefWidth(150 + 50 * factor);
                        button.setPrefHeight(150 + 50 * factor);
                        button.getButton().setRadius(75 + 25 * factor);
                    } else {
                        button.setPrefWidth(150);
                        button.setPrefHeight(150);
                        button.getButton().setRadius(75);
                    }
                }
            }
        });
    }

    public void createCircularButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPrefWidth(150);
            buttons.get(i).setPrefHeight(150);
            buttons.get(i).getButton().setRadius(75);
            buttons.get(i).layoutXProperty().bind(closeMenuButton.layoutXProperty().add(250 * Math.cos(Math.toRadians(i * 360d / buttons.size()))).subtract(50));
            buttons.get(i).layoutYProperty().bind(closeMenuButton.layoutYProperty().add(250 * Math.sin(Math.toRadians(i * 360d / buttons.size()))).subtract(50));
            this.getChildren().add(buttons.get(i));

            int index = i;
            buttons.get(i).setOnMouseEntered(event -> {
                buttons.get(index).requestFocus();
            });
        }
    }

}
