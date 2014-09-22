package de.saxsys.cwgol;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author sven.hoffmann
 */
public class MainApplication extends Application {

    private static final double ALIVE_PROBABILITY = 0.35;

    private static final int FPS = 30;

    private static final int SQUARE_SIZE = 4; // pixels
    private static final int X_MAX = 400; // cells
    private static final int Y_MAX = 225; // cells

    private boolean[][] currentGeneration = new boolean[X_MAX][Y_MAX];
    private boolean[][] nextGeneration = new boolean[X_MAX][Y_MAX];

    private Rectangle[][] content = new Rectangle[X_MAX][Y_MAX];

    private void seed(double aliveProbability) {
        for (int i = 0; i < X_MAX; i++) {
            for (int j = 0; j < Y_MAX; j++) {
                nextGeneration[i][j] = Math.random() < aliveProbability;
            }
        }
    }

    private void createNewGeneration() {
        for (int i = 0; i < X_MAX; i++) {
            for (int j = 0; j < Y_MAX; j++) {
                nextGeneration[i][j] = getRuleSetResult(i, j);
            }
        }
    }

    private boolean getRuleSetResult(int x, int y) {
        if (currentGeneration[x][y]) {
            return aliveCellRules(x, y);
        } else {
            return deadCellRules(x, y);
        }
    }

    private boolean aliveCellRules(int x, int y) {
        switch (countAliveNeighbors(x, y)) {
            case 0: case 1: return false;
            case 2: case 3: return true;
            default: return false;
        }
    }

    private boolean deadCellRules(int x, int y) {
        switch (countAliveNeighbors(x, y)) {
            case 3: return true;
            default: return false;
        }
    }

    private int countAliveNeighbors(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1 && i < X_MAX; i++) {
            for (int j = y - 1; j <= y + 1 && j < Y_MAX; j++) {
                if (i >= 0 && j >= 0 && !(i == x && j == y) && currentGeneration[i][j]) {
                    count++;
                }
                if (count == 4) {
                    return count;
                }
            }
        }
        return count;
    }

    private void renderNextGeneration() {
        for (int i = 0; i < X_MAX; i++) {
            for (int j = 0; j < Y_MAX; j++) {
                Rectangle cell = content[i][j];
                if (nextGeneration[i][j] && !currentGeneration[i][j]) {
                    cell.setFill(Color.BLACK);
                } else if (!nextGeneration[i][j] && currentGeneration[i][j]) {
                    cell.setFill(Color.WHITE);
                }
            }
        }

        // exchange currentGeneration and nextGeneration arrays
        boolean[][] temporaryReference = currentGeneration;
        currentGeneration = nextGeneration;
        nextGeneration = temporaryReference;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        HBox hBox = new HBox(0);
        for (int i = 0; i < X_MAX; i++) {
            VBox vBox = new VBox(0);
            hBox.getChildren().add(vBox);
            for (int j = 0; j < Y_MAX; j++) {
                Rectangle cell = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.WHITE);
                vBox.getChildren().add(cell);
                content[i][j] = cell;
            }
        }

        final String titleTemplate = "Conway's Game of Life | Generation: %d";

        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.setTitle(String.format(titleTemplate, 0));

        seed(ALIVE_PROBABILITY);
        renderNextGeneration();

        stage.show();

        final Duration durationOfFps = Duration.millis(1000 / FPS);
        final KeyFrame keyFrame = new KeyFrame(durationOfFps,
                new EventHandler<ActionEvent>() {
                    private long generation = 1;

                    @Override
                    public void handle(ActionEvent event) {
                        createNewGeneration();
                        renderNextGeneration();
                        stage.setTitle(String.format(titleTemplate, generation));
                        generation++;
                    }
                });

        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
