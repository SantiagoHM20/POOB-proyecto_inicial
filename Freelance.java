
/**
 * Write a description of class Freelance here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Freelance extends Tile
{
    /**
     * Constructor para crear una Tile de tipo Freelance
     * No se pega
     *
     * @param width       Ancho del agujero.
     * @param height      Altura del agujero.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     */
    public Freelance(int width, int height, int xPosition, int yPosition) {
        super(width, height, xPosition, yPosition, "gray", 'f');
        setTypet("Freelance");
    }
}