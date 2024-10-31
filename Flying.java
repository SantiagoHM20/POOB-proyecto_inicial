
/**
 * Write a description of class Flying here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Flying extends Tile
{
   /**
     * Constructor para crear una Tile de tipo Flying
     * No se pega
     *
     * @param width       Ancho del agujero.
     * @param height      Altura del agujero.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     */
    public Flying(int width, int height, int xPosition, int yPosition) {
        super(width, height, xPosition, yPosition, "gray", 'l');
        setTypet("Flying");
    }
}
