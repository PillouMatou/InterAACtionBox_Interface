package main;

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
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.gaze.devicemanager.GazeEvent;

@Slf4j
public class ProgressButton extends StackPane {

    @Getter
    private final Circle button;
    private ProgressIndicator indicator;
    private Timeline timelineProgressBar;
    private double buttonHeight;
    private EventHandler<Event> enterbuttonHandler;
    private EventHandler<Event> exitbuttonHandler;
    private boolean inuse = false;

    private VBox imageAndText;

    public ProgressButton() {
        super();
        button = new Circle();
        button.setFill(Color.LIGHTGREY);
        init();
        ImageView image = new ImageView();
        Label text = new Label();
        this.imageAndText = new VBox(image, text);
        this.imageAndText.setMouseTransparent(true);
        this.imageAndText.setAlignment(Pos.CENTER);
        this.getChildren().addAll(button, imageAndText, indicator);
    }

    public ImageView getImage() {
        return (ImageView) imageAndText.getChildren().get(0);
    }

    public Label getLabel() {
        return (Label) imageAndText.getChildren().get(1);
    }

    public void active() {
        inuse = true;
        this.setOpacity(1);
        this.indicator.setOpacity(0);
        this.setDisable(false);
        this.button.setDisable(false);
    }

    public void disable(final boolean b) {
        if (b) {
            disable();
        } else {
            active();
        }
    }

    public void disable() {
        inuse = false;
        this.setOpacity(0);
        this.setDisable(true);
        this.button.setDisable(true);
    }

    public void setImage(final ImageView img) {
        img.setMouseTransparent(true);
        this.imageAndText.getChildren().set(0, img);
    }

    public void active2() {
//        this.button.addEventFilter(GazeEvent.GAZE_ENTERED, enterbuttonHandler);
//        this.button.addEventFilter(GazeEvent.GAZE_EXITED, exitbuttonHandler);
        this.setDisable(false);
        this.button.setDisable(false);
        this.setOpacity(1);
        this.indicator.setOpacity(0);

        this.button.addEventFilter(MouseEvent.MOUSE_ENTERED, enterbuttonHandler);
        this.button.addEventFilter(MouseEvent.MOUSE_EXITED, exitbuttonHandler);
    }

    public void init() {
        final double buttonWidth = 0;
        buttonHeight = 0;
        indicator = new ProgressIndicator(0);
        indicator.setMouseTransparent(true);
        timelineProgressBar = new Timeline();
        button.radiusProperty().addListener((obs, oldVal, newVal) -> {
            indicator.setMinHeight(2 * newVal.doubleValue());
            indicator.setMinWidth(2 * newVal.doubleValue());
            buttonHeight = newVal.doubleValue();
            double width = newVal.doubleValue() * 2;
            width = (width * 90) / 100;
            this.getImage().setFitWidth(width);
        });

        indicator.setOpacity(0);
    }

    public ProgressIndicator assignIndicator(final EventHandler<Event> enterEvent, final int fixationLength) {
        indicator.setMouseTransparent(true);
        indicator.setOpacity(0);
        final ProgressButton pb = this;
        final Event e1 = new Event(pb, pb, MouseEvent.MOUSE_CLICKED);

        enterbuttonHandler = e -> {
            if (inuse) {
                indicator.setProgress(0);
                indicator.setOpacity(0.5);

                timelineProgressBar.stop();
                timelineProgressBar.getKeyFrames().clear();

                timelineProgressBar.setDelay(new Duration(300));

                timelineProgressBar.getKeyFrames().add(
                        new KeyFrame(new Duration(fixationLength), new KeyValue(indicator.progressProperty(), 1)));

                timelineProgressBar.onFinishedProperty().set(actionEvent -> {
                    indicator.setOpacity(0);
                    if (enterEvent != null) {
                        enterEvent.handle(e1);
                    }
                });
                timelineProgressBar.play();
            }
        };

        exitbuttonHandler = e -> {
            if (inuse) {

                timelineProgressBar.stop();
                indicator.setOpacity(0);
                indicator.setProgress(0);
            }
        };

        active2();

        return indicator;
    }

}
