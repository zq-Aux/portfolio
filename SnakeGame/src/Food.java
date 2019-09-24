import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class Food {
    private double xPos;
    private double yPos;
    private double unit;
    private int boardLength;

    double xPosition() {
        return xPos;
    }

    double yPosition() {
        return yPos;
    }

    Food(double x, double y, double unit, int boardLength) {
        this.unit = unit;
        this.boardLength = boardLength;
        xPos = x;
        yPos = y;
    }

    Food(double unit, int boardLength) {
        this.unit = unit;
        this.boardLength = boardLength;
    }

    /**
     * An object to return the food object and randomize the position of the object, if needed.
     *
     * @param fed - indicates whether the food's position needs to be moved to a different location due to being eaten
     * @param color - indicates what color the food should be to correspond to the team of the snake it belongs to
     * @return - a circle object to represent the food.
     */
    Circle generateCircle(boolean fed, Color color) {
        if (fed) {
            xPos = (((int) ((Math.random() * (boardLength))) * unit) + (unit / 2));
            yPos = (((int) ((Math.random() * (boardLength))) * unit) + (unit / 2));
        }
        return new Circle(xPos, yPos, unit / 3, color);
    }
}
