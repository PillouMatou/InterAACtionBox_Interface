package main.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class I18NProgressButton extends StackPane {

    private static final int FIXATION_LENGTH = 3000;

    @Getter
    private final Circle button;
    private final VBox imageAndText;
    private final ProgressIndicator indicator;

    private final Timeline timelineProgressBar;

    private EventHandler<Event> enterButtonHandler;
    private EventHandler<Event> exitButtonHandler;

    private boolean started = false;

    public I18NProgressButton() {
        super();
        timelineProgressBar = new Timeline();

        button = new Circle();
        button.setFill(Color.web("#faeaed"));

        ImageView image = new ImageView();
        image.setPreserveRatio(true);

        Label text = new Label();

        text.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        text.setTextFill(Color.web("#cd2653"));
        text.setAlignment(Pos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);

        imageAndText = new VBox(image, text);
        imageAndText.setMouseTransparent(true);
        imageAndText.setAlignment(Pos.CENTER);

        indicator = new ProgressIndicator(0);
        indicator.setMouseTransparent(true);
        indicator.setOpacity(0);

        this.getChildren().addAll(button, imageAndText, indicator);

        button.radiusProperty().addListener((obs, oldVal, newVal) -> {
            indicator.setMinHeight(2 * newVal.doubleValue());
            indicator.setMinWidth(2 * newVal.doubleValue());
            double width = newVal.doubleValue() * 2;
            this.getImage().setFitWidth((99 * width) / 100);
        });
    }

    public ImageView getImage() {
        return (ImageView) imageAndText.getChildren().get(0);
    }

    public void setImage(final ImageView img) {
        img.setMouseTransparent(true);
        this.imageAndText.getChildren().set(0, img);
    }

    public void setSpacing(double value) {
        this.imageAndText.setTranslateY(value);
        this.imageAndText.setSpacing(value);
    }

    public Label getLabel() {
        return (Label) imageAndText.getChildren().get(1);
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
                        new KeyFrame(new Duration(I18NProgressButton.FIXATION_LENGTH), new KeyValue(indicator.progressProperty(), 1)));

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

    public void deactivate(){
        timelineProgressBar.stop();
        indicator.setOpacity(0);
        indicator.setProgress(0);
        this.button.removeEventFilter(MouseEvent.MOUSE_ENTERED, enterButtonHandler);
        this.button.removeEventFilter(MouseEvent.MOUSE_EXITED, exitButtonHandler);
    }
}
