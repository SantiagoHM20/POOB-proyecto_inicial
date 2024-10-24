/**
 * Clase que representa una ficha en el juego.
 */
public class Tile {
    private Rectangle rectangle;  
    private char type;          
    private String typet;        
    private boolean glued;       

    /**
     * Constructor para crear una ficha.
     *
     * @param width       Ancho de la ficha.
     * @param height      Altura de la ficha.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     * @param color       Color de la ficha.
     * @param type        Tipo de ficha.
     */
    public Tile(int width, int height, int xPosition, int yPosition, String color, char type) {
        this.rectangle = new Rectangle(width, height, xPosition, yPosition, color);
        this.type = type;
        this.typet = "ficha"; 
        this.glued = false; 
    }

    /**
     * Constructor para crear un agujero.
     *
     * @param width       Ancho del agujero.
     * @param height      Altura del agujero.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     * @param color       Color del agujero.
     * @param typet       Tipo del objeto (debe ser "hole").
     */
    public Tile(int width, int height, int xPosition, int yPosition, String color, String typet) {
        this.rectangle = new Rectangle(width, height, xPosition, yPosition, color);
        this.typet = "hole"; 
        this.glued = false;
    }

    /**
     * Muestra las fichas unidas (sin implementar).
     */
    public void showGlue() {
        
    }

    /**
     * Verifica si la ficha está unida a otra.
     *
     * @return true si la ficha está unida, false en caso contrario.
     */
    public boolean isGlued() {
        return glued;
    }

    /**
     * Establece el estado de unión de la ficha.
     *
     * @param glued true para marcar la ficha como unida, false para desunirla.
     */
    public void setGlued(boolean glued) {
        this.glued = glued;
    }

    /**
     * Obtiene el tipo del objeto (ficha o agujero).
     *
     * @return tipo de objeto como cadena.
     */
    public String getTypet() {
        return typet;
    }

    /**
     * Obtiene el color de la ficha.
     *
     * @return color de la ficha.
     */
    public String getColor() {
        return rectangle.getColor(); 
    }

    /**
     * Obtiene el ancho de la ficha.
     *
     * @return ancho de la ficha.
     */
    public int getWidth() {
        return rectangle.getWidth();
    } 

    /**
     * Obtiene la altura de la ficha.
     *
     * @return altura de la ficha.
     */
    public int getHeight() {
        return rectangle.getHeight();
    }

    /**
     * Establece la posición de la ficha.
     *
     * @param x nueva posición en el eje X.
     * @param y nueva posición en el eje Y.
     */
    public void setPosition(int x, int y) {
        this.rectangle.setX(x);
        this.rectangle.setY(y);
    }

    /**
     * Hace visible la ficha.
     */
    public void makeVisible() {
        rectangle.makeVisible();
    }

    /**
     * Hace invisible la ficha.
     */
    public void makeInvisible() {
        rectangle.makeInvisible();
    }

    /**
     * Cambia el color de la ficha.
     *
     * @param newColor nuevo color para la ficha.
     */
    public void changeColor(String newColor) {
        rectangle.changeColor(newColor);
    }

    /**
     * Obtiene la posición en el eje X de la ficha.
     *
     * @return posición en el eje X.
     */
    public int getXPosition() {
        return rectangle.getXPosition();
    }

    /**
     * Obtiene la posición en el eje Y de la ficha.
     *
     * @return posición en el eje Y.
     */
    public int getYPosition() {
        return rectangle.getYPosition();
    }

    /**
     * Obtiene el tipo de ficha.
     *
     * @return tipo de ficha.
     */
    public char getType() {
        return type;
    }

    /**
     * Establece el tipo de ficha.
     *
     * @param type nuevo tipo de ficha.
     */
    public void setType(char type) {
        this.type = type;
    }

    /**
     * Obtiene la fila en la que se encuentra la ficha.
     *
     * @return fila de la ficha.
     */
    public int getRow() {
        return getYPosition() / 50; 
    }

    /**
     * Obtiene la columna en la que se encuentra la ficha.
     *
     * @return columna de la ficha.
     */
    public int getColumn() {
        return getXPosition() / 50;
    }
}