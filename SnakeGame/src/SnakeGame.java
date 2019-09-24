import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class SnakeGame extends Application {
    private static final double unit = 40.0;
    private static final int boardLength = 20;
    private String snakeOneImage = "black.png";
    private String snakeTwoImage = "pinwheel.png";
    private String floorImage = "floor.png";
    private BackgroundImage floor = new BackgroundImage(new Image(new File(floorImage).toURI().toString()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private Background background = new Background(floor);
    private Snake snake = new Snake(((int) (boardLength / 3)) * unit, ((int) (boardLength / 2)) * unit, 3, new Image(new File(snakeOneImage).toURI().toString()), Color.RED, unit, boardLength);
    private Snake snake2 = new Snake(((int) (boardLength / 3 * 2)) * unit, ((int) (boardLength / 2)) * unit, 3, new Image(new File(snakeTwoImage).toURI().toString()), Color.BLUE, unit, boardLength);
    private Pane root;
    private static int tempX, tempY, tempX2, tempY2;

    public static void main(String[] args) {
        launch(args);
    }

    private HBox generateGameBar() {
        HBox gameBar = new HBox();
        Font scoreFont = new Font(unit / 2);
        Text score1 = new Text("Red Score: " + snake.getScore());
        score1.setFill(Color.RED);
        score1.setFont(scoreFont);
        Text score2 = new Text("Blue Score: " + snake2.getScore());
        score2.setFill(Color.BLUE);
        score2.setFont(scoreFont);

        gameBar.setSpacing(unit);
        gameBar.setPadding(new Insets(10, 10, 10, 10));

        gameBar.getChildren().addAll(score1, score2);

        return gameBar;
    }

    public void run(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, unit * boardLength, unit * boardLength);
        primaryStage.setScene(scene);
        root.setBackground(background);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.seconds(.2),
                        event -> {
                            root.getChildren().clear();
                            for (Circle circle : snake.makeCircles()) {
                                root.getChildren().addAll(circle);
                            }
                            for (Circle circle : snake2.makeCircles()) {
                                root.getChildren().addAll(circle);
                            }
                            for (int i = 0; i < boardLength * unit; i += unit) {
                                root.getChildren().add(new Line(i, 0, i, boardLength * unit));
                            }
                            for (int j = 0; j < boardLength * unit; j += unit) {
                                root.getChildren().add(new Line(0, j, boardLength * unit, j));
                            }

                            root.getChildren().add(snake.generateFood());
                            root.getChildren().add(snake2.generateFood());
                            snake.setP2Coords(snake2.getCoords());
                            snake2.setP2Coords(snake.getCoords());
                            snake.move();
                            snake2.move();
                            resetTemp();
                            root.getChildren().add(generateGameBar());

                        } //timeline event for animation
                )
        );
        timeline.play();

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    if (tempY != 1) {
                        snake.setDirection(0, -1);
                    }
                    break;
                case A:
                    if (tempX != 1) {
                        snake.setDirection(-1, 0);
                    }
                    break;
                case S:
                    if (tempY != -1) {
                        snake.setDirection(0, 1);
                    }
                    break;
                case D:
                    if (tempX != -1) {
                        snake.setDirection(1, 0);
                    }
                    break;
                case I:
                    if (tempY2 != 1) {
                        snake2.setDirection(0, -1);
                    }
                    break;
                case J:
                    if (tempX2 != 1) {
                        snake2.setDirection(-1, 0);
                    }
                    break;
                case K:
                    if (tempY2 != -1) {
                        snake2.setDirection(0, 1);
                    }
                    break;
                case L:
                    if (tempX2 != -1) {
                        snake2.setDirection(1, 0);
                    }
                    break;
                case ESCAPE:
                    System.exit(0);
                    break;
            }
        }); //input controls
    }

    private void resetTemp() {
        tempX = snake.dirxProperty();
        tempY = snake.diryProperty();
        tempX2 = snake2.dirxProperty();
        tempY2 = snake2.diryProperty();
    } //maintains current direction heading to prevent logic errors when changing directions
    // rapidly

    @Override
    public void start(Stage primaryStage) throws Exception {
        run(primaryStage);
        primaryStage.show();
    }
}
