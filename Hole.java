public class Hole extends Tile {

    /**
     * Constructor para crear un agujero.
     *
     * @param width       Ancho del agujero.
     * @param height      Altura del agujero.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     */
    public Hole(int width, int height, int xPosition, int yPosition) {
        super(width, height, xPosition, yPosition, "white", 'h');
        setTypet("hole");
    }
}