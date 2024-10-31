
/**
 * Write a description of class Rough here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rough extends Tile
{
   /**
     * Constructor para crear una Tile de tipo Rough
     * No desliza
     *
     * @param width       Ancho del agujero.
     * @param height      Altura del agujero.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     */
    public Rough(int width, int height, int xPosition, int yPosition) {
        super(width, height, xPosition, yPosition, "magenta", 'r');
        setTypet("Rough");
    }
}