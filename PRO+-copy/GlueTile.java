import java.util.ArrayList;
import java.util.List;

public class GlueTile extends Tile {
    private List<Tile> gluedTiles; // Lista de baldosas pegadas

    // Cambiamos el constructor para que reciba char en lugar de String
    public GlueTile(int row, int column, char colorChar) {
        super(row, column, colorChar); // Llama al constructor de Tile con colorChar
        gluedTiles = new ArrayList<>();
        gluedTiles.add(this); // La baldosa en sí misma es parte del grupo pegado
    }

    public void addGluedTile(Tile tile) {
        if (!gluedTiles.contains(tile)) {
            gluedTiles.add(tile);
        }
    }

    public List<Tile> getGluedTiles() {
        return gluedTiles;
    }
}
