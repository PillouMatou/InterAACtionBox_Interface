package main.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.utils.UtilsUI;

@Slf4j
public class ProgressDoubleClickedButton extends StackPane {

    private static final int FIXATION_LENGTH = 500;

    @Getter
    private final Button button;
    private final ProgressIndicator indicator;

    private final Timeline timelineProgressBar;

    private EventHandler<Event> enterButtonHandler;
    private EventHandler<Event> exitButtonHandler;

    private boolean started = false;

    public ProgressDoubleClickedButton(String name, String imagePath, EventHandler eventhandler, Stage primaryStage) {
        super();
        timelineProgressBar = new Timeline();

        button = UtilsUI.getDoubleClickedButton(name, imagePath, eventhandler, primaryStage);

        ImageView image = new ImageView();
        image.setPreserveRatio(true);


        indicator = new ProgressIndicator(0);
        indicator.setMouseTransparent(true);
        indicator.setOpacity(0);

        this.getChildren().addAll(button, indicator);

        button.heightProperty().addListener((obs, oldVal, newVal) -> {
            indicator.setMinHeight(newVal.doubleValue());
            indicator.setMinWidth(newVal.doubleValue());
        });
    }

    private void activate() {
        enable();
        this.button.addEventFilter(MouseEvent.MOUSE_ENTERED, enterButtonHandler);
        this.button.addEventFilter(MouseEvent.MOUSE_EXITED, exitButtonHandler);
    }

    private void enable() {
        this.setOpacity(1);
        this.setDisable(false);
        this.button.setDisable(false);
    }

    private void disable() {
        this.setOpacity(0);
        this.setDisable(true);
        this.button.setDisable(true);
    }

    public void disable(final boolean isDisable) {
        if (isDisable) {
            disable();
        } else {
            enable();
        }
    }

    public void start() {
        disable(false);
        started = true;
        this.indicator.setOpacity(0);
    }

    public void stop() {
        disable(true);
        started = false;
    }

    public void assignIndicator(final EventHandler<Event> eventHandler) {
        indicator.setMouseTransparent(true);
        indicator.setOpacity(0);

        enterButtonHandler = enterEvent -> {
            if (started) {
                timelineProgressBar.stop();
                indicator.setOpacity(0.5);
                indicator.setProgress(0);

                timelineProgressBar.getKeyFrames().clear();
                timelineProgressBar.setDelay(new Duration(500));
                timelineProgressBar.getKeyFrames().add(
                        new KeyFrame(new Duration(ProgressDoubleClickedButton.FIXATION_LENGTH), new KeyValue(indicator.progressProperty(), 1)));

                timelineProgressBar.onFinishedProperty().set(actionEvent -> {
                    indicator.setOpacity(0);
                    if (eventHandler != null) {
                        eventHandler.handle(new Event(this, this, MouseEvent.MOUSE_CLICKED));
                    }
                });

                timelineProgressBar.play();
            }
        };

        exitButtonHandler = exitEvent -> {
            if (started) {
                timelineProgressBar.stop();
                indicator.setOpacity(0);
                indicator.setProgress(0);
            }
        };

        activate();
    }

}
