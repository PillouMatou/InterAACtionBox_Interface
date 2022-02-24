package main.gaze.devicemanager;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import tobii.Tobii;

import java.awt.*;

@Slf4j
public class PositionPollerRunnable implements Runnable {

    private final Configuration configuration;
    Robot robot = new Robot();
    @Setter
    private transient boolean stopRequested = false;

    public PositionPollerRunnable(Configuration configuration) throws AWTException {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        while (!stopRequested) {
            try {
                if (configuration.isGazeInteraction()) {
                    configuration.analyse(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
                    poll();
                }
            } catch (final RuntimeException e) {
                log.warn("Exception while polling position of main.gaze", e);
            }
            // sleep is mandatory to avoid too much calls to gazePosition()
            try {
                Thread.sleep(10);
            } catch (InterruptedException | RuntimeException e) {
                log.warn("Exception while sleeping until next poll", e);
            }
        }
    }

    private void poll() {
        final float[] pointAsFloatArray = Tobii.gazePosition();

        final float xRatio = pointAsFloatArray[0];
        final float yRatio = pointAsFloatArray[1];

        final Rectangle2D screenDimension = Screen.getPrimary().getBounds();
        final double positionX = xRatio * screenDimension.getWidth();
        final double positionY = yRatio * screenDimension.getHeight();


        final double offsetX = 0;
        final double offsetY = 0;

        if (configuration.waitForUserMove()) {

            if(xRatio != 0.5 && yRatio != 0.5) {
                final Point2D point = new Point2D(positionX + offsetX, positionY + offsetY);
                robot.mouseMove((int) point.getX(), (int) point.getY());
                configuration.currentPoint.add(new Point2D(MouseInfo.getPointerInfo().getLocation().getX(),
                        MouseInfo.getPointerInfo().getLocation().getY()));
            }else{
                final Point2D point = new Point2D(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
                robot.mouseMove((int) point.getX(), (int) point.getY());
                configuration.currentPoint.add(new Point2D(MouseInfo.getPointerInfo().getLocation().getX(),
                        MouseInfo.getPointerInfo().getLocation().getY()));
            }
        } else {
            configuration.updateLastPositions();
        }
    }

}
