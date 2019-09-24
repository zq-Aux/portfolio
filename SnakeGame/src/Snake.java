import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Point2D> coords = new ArrayList<>();
    private ArrayList<Point2D> p2Coords = new ArrayList<>();
    private double unit;
    private int boardLength;
    private boolean isAlive = false;
    private boolean fed = false;
    private Food food = null;
    private int directionX = 0;
    private int directionY = -1;
    private Color color = Color.BLACK;
    private Image image;
    private int score;
    private boolean isSinglePlayer;
    private String chompFile = "chomp.mp3";
    private AudioClip chompSound = new AudioClip(new File(chompFile).toURI().toString());

    ArrayList<Point2D> getCoords() {
        return coords;
    }

    void setP2Coords(ArrayList<Point2D> coords) {
        p2Coords = coords;
    }

    int getScore() {
        return score;
    }

    int diryProperty() {
        return directionY;
    }

    int dirxProperty() {
        return directionX;
    }

    /**
     * Sets the direction heading of the snake for use in the move function.
     *
     * @param dirX - sets the X direction heading of the snake
     * @param dirY - sets the Y direction heading of the snake
     */
    void setDirection(int dirX, int dirY) {
        directionX = dirX;
        directionY = dirY;
    }

    /**
     * Generates a snake object starting at the center of the board with only a predefined initial length.
     *
     * @param length      - Defines initial length of snake
     * @param unit        - Provides the unit of the board for scale
     * @param boardLength - Provides the number of units per board length
     */
    Snake(int length, double unit, int boardLength) {
        this.unit = unit;
        this.boardLength = boardLength;
        for (int i = 0; i < length; i++)
            coords.add(new Point2D(unit * boardLength / 2, unit * boardLength / 2));
        food = new Food(unit, boardLength);
        food.generateCircle(true, color);
        isAlive = true;
        isSinglePlayer = false;
    }

    /**
     * Constructor for snake with pre-defined coordinates, and length.
     *
     * @param x           - Defines starting x-coordinate
     * @param y           - Defines starting y-coordinate
     * @param length      - Defines initial length
     * @param unit        - Provides unit for scale
     * @param boardLength - Provides number of units per board length
     */
    Snake(double x, double y, int length, double unit, int boardLength) {
        this.unit = unit;
        this.boardLength = boardLength;
        for (int i = 0; i < length; i++)
            coords.add(new Point2D(x, y));
        food = new Food(unit, boardLength);
        food.generateCircle(true, color);
        isAlive = true;
        isSinglePlayer = false;
    }

    /**
     * Constructor for snake with pre-defined coordinates, length, and color.
     *
     * @param x           - Defines starting x-coordinate
     * @param y           - Defines starting y-coordinate
     * @param length      - Defines initial length
     * @param image       - Defines initial image
     * @param unit        - Provides unit for scale
     * @param boardLength - Provides number of units per board length
     */
    Snake(double x, double y, int length, Image image, Color color, double unit, int boardLength) {
        this.unit = unit;
        this.boardLength = boardLength;
        this.image = image;
        this.color = color;
        isSinglePlayer = false;
        for (int i = 0; i < length; i++)
            coords.add(new Point2D(x, y));
        food = new Food(x, (y - unit * 2), unit, boardLength);
        food.generateCircle(true, color);
        isAlive = true;
    }

    /**
     * Generates an array of circles at each snake body coordinate.
     *
     * @return - an array of circles centered at each coordinate
     */
    Circle[] makeCircles() {
        Circle[] body = new Circle[coords.size()];
        for (int i = 0; i < coords.size(); i++) {
            Circle circle = new Circle(coords.get(i).getX() + unit / 2, coords.get(i).getY() + unit / 2, unit / 2);
            circle.setFill(new ImagePattern(image));
            body[i] = circle;
        }
        return body;
    }

    /**
     * Adds a circle to the front of the snake in the indicated direction and removes the circle at the end of the tail.
     */
    void move() {
        Point2D testPoint = new Point2D(coords.get(0).getX() + unit * directionX, coords.get(0).getY() + unit * directionY);
        if (checkMove(testPoint) && isAlive) {
            coords.add(0, testPoint);
            if (!fed) {
                coords.remove(coords.size() - 1);
            } else {
                do {
                    food.generateCircle(true, color);
                } while (checkPos(new Point2D(food.xPosition(), food.yPosition())));
                fed = false;
            }
        }
    }

    /**
     * Method to test if a point is the same as any point on any snake
     *
     * @param testPoint - the point to test
     * @return - if the point is on the same grid space as another point on a snake
     */
    private boolean checkPos(Point2D testPoint) {
        for (Point2D point : coords) {
            if (testPoint.distance(point) < unit) {
                return true;
            }
        }
        if (!isSinglePlayer) {
            for (Point2D point : p2Coords) {
                if (testPoint.distance(point) < unit) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A method used to check the validity of a an attempted move.
     *
     * @return one of three values describing if the move is valid, invalid, or null
     * (trying to move to the previous coordinate / the opposite of the current direction).
     */
    private boolean checkMove(Point2D checkPoint) {
        for (Point2D point : coords) {
            if (checkPoint.equals(point) || checkPoint.getX() < 0 || checkPoint.getX() > (unit * boardLength) - unit || checkPoint.getY() < 0 || checkPoint.getY() > (unit * boardLength) - unit) {
                if (checkPoint.equals(coords.get(0))) {
                    return false;
                }
                if (coords.get(coords.size() - 1) == point) {
                    return true;
                }
                isAlive = false;
            }
        }
        if ((checkPoint.distance(new Point2D(food.xPosition() - (unit / 2), food.yPosition() - (unit / 2))) < unit) && isAlive) {
            fed = true;
            score++;
            chompSound.play();
        }
        if (!isSinglePlayer) {
            for (Point2D point : p2Coords) {
                if (checkPoint.equals(point) || checkPoint.getX() < 0 || checkPoint.getX() > (unit * boardLength) - unit || checkPoint.getY() < 0 || checkPoint.getY() > (unit * boardLength) - unit) {
                    if (coords.get(coords.size() - 1) == point) {
                        return true;
                    }
                    isAlive = false;
                }
            }
        }
        return true;
    }

    /**
     * @return - the circle object for this snake's food
     */
    Circle generateFood() {
        return food.generateCircle(false, color);
    }
}
