
public class Tile {
    private Rectangle rectangle; // Composición en lugar de herencia
    private char type; // Tipo de ficha (ej. 'r', 'b', 'y', 'g')

    public Tile(int width, int height, int xPosition, int yPosition, String color, char type) {
        this.rectangle = new Rectangle(width, height, xPosition, yPosition, color);
        this.type = type;
    }

    // Métodos para manipular el rectángulo
    public void makeVisible() {
        rectangle.makeVisible();
    }

    public void makeInvisible() {
        rectangle.makeInvisible();
    }

    public void changeColor(String newColor) {
        rectangle.changeColor(newColor);
    }

    public int getXPosition() {
        return rectangle.getXPosition();
    }

    public int getYPosition() {
        return rectangle.getYPosition();
    }

    // Getter y setter para el tipo de ficha
    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
}