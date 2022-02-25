package main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.LinkedList;

public class Configuration {

    public final static String VERSION = "InterAACtionBox-Interface 1.0.0 Dev v.2021.18.11";

    public final static int MOUSE_INTERACTION = 0;
    public final static int GAZE_INTERACTION = 1;
    @Setter
    public Scene scene;
    public int numberOfLastPositionsToCheck = 7;
    public LinkedList<Point2D> lastPositions = new LinkedList<>();
    public LinkedList<Point2D> currentPoint = new LinkedList<>();
    IntegerProperty selectionMode = new SimpleIntegerProperty(Configuration.MOUSE_INTERACTION);
    boolean userIsMoving = false;
    @Getter
    @Setter
    public String language = "eng";

    public Configuration() {
    }

    public boolean isGazeInteraction() {
        return selectionMode.intValue() == Configuration.GAZE_INTERACTION;
    }

    public boolean waitForUserMove() {
        return !userIsMoving || lasPositionDidntMoved();
    }

    public void analyse(double x, double y) {
        if (
                (currentPoint != null && currentPoint.size() > 0
                        && !isArround(x, currentPoint.getLast().getX()) && !isArround(y, currentPoint.getLast().getY()))
        ) {
            this.userIsMoving = true;
        }
    }

    public boolean isArround(double coord0, double coord1) {
        return coord0 <= coord1 + 2 && coord0 >= coord1 - 2;
    }

    public void updateLastPositions() {
        while (lastPositions.size() >= numberOfLastPositionsToCheck) {
            lastPositions.pop();
        }
        lastPositions.add(new Point2D(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY()));
    }

    public void setMode(int newMode) {
        selectionMode.setValue(newMode);
    }

    public boolean lasPositionDidntMoved() {
        if (lastPositions.size() == numberOfLastPositionsToCheck) {
            Point2D pos = lastPositions.get(0);
            for (int i = 0; i < numberOfLastPositionsToCheck; i++) {
                if (!lastPositions.get(i).equals(pos)) {
                    return false;
                }
            }
            lastPositions.clear();
            this.userIsMoving = false;
            return true;
        }
        return false;
    }
}
