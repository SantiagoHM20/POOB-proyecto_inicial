public class Fixed extends Tile {

    /**
     * Constructor para crear una Tile de tipo Fixed
     * No se deja eliminar ni reubicar
     *
     * @param width       Ancho del agujero.
     * @param height      Altura del agujero.
     * @param xPosition   Posición en el eje X.
     * @param yPosition   Posición en el eje Y.
     */
    public Fixed(int width, int height, int xPosition, int yPosition) {
        super(width, height, xPosition, yPosition, "esmerald", 'F');
        setTypet("Fixed");
    }
}