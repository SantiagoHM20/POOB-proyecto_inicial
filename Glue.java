
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
/**
 * Clase que representa el pegamento en el juego de rompecabezas.
 * Maneja las operaciones relacionadas con la adhesión y separación de fichas.
 */
public class Glue {
     private static ArrayList<ArrayList<Tile>> gluedGroups = new ArrayList<>();
     private static Map<Tile, Rectangle> glueVisuals = new HashMap<>();

    public Glue() {
        this.gluedGroups = new ArrayList<>();
    }

/**
 * Añade pegamento a la ficha en la posición especificada.
 * Verifica si la ficha ya está pegada y gestiona los grupos de pegamento.
 *
 * @param tile la ficha a pegar
 * @param adjacents las fichas adyacentes a considerar para el pegado
 */
public static void addGlue(Tile tile, ArrayList<Tile> adjacents) {
   
    if (isGlued(tile)) {
        System.out.println("La ficha ya está pegada");
        return;
    }
    
    
    if (tile.getTypet().equals("Freelance")) {
        System.out.println("Freelance no puede ser pegada");
        return;
    }

    ArrayList<ArrayList<Tile>> mergeGroup = new ArrayList<>();
    ArrayList<Tile> finalGroup = new ArrayList<>();
    finalGroup.add(tile);

   
    ArrayList<Tile> freelancesToRemove = new ArrayList<>();


    for (Tile adjacentTile : adjacents) {
      
        if (adjacentTile.getTypet().equals("Freelance")) {
            freelancesToRemove.add(adjacentTile); 
        } else {
          
            if (isGlued(adjacentTile)) {
                for (ArrayList<Tile> group : gluedGroups) {
                    if (group.contains(adjacentTile)) {
                        mergeGroup.add(group);
                        break;
                    }
                }
            } else {
                finalGroup.add(adjacentTile); 
            }
        }
    }

  
    adjacents.removeAll(freelancesToRemove);

 
    for (ArrayList<Tile> group : mergeGroup) {
        finalGroup.addAll(group);
    }


    gluedGroups.removeAll(mergeGroup);
    gluedGroups.add(finalGroup);
    showGlue(tile);
}

    /**
     * Elimina el pegamento de la ficha especificada y actualiza los grupos de pegamento.
     *
     * @param tile la ficha a la que se le eliminará el pegamento
     * @param adjacents las fichas adyacentes de la ficha
     */
    public static void deleteGlue(Tile tile, ArrayList<Tile> adjacents) {
        if (!isGlued(tile)) {
            System.out.println("La ficha no está pegada");
            return;
        }

        ArrayList<Tile> originalGlueGroup = getGlueGroup(tile);
        if (originalGlueGroup != null) {
            originalGlueGroup.remove(tile);
        }

        // Opcional: Aquí podrías reordenar los grupos en base a las fichas adyacentes después de quitar el pegamento
    }

    /**
     * Verifica si una ficha está pegada en alguno de los grupos de pegamento.
     *
     * @param tile la ficha a verificar
     * @return true si la ficha está pegada, false en caso contrario
     */
    public static boolean isGlued(Tile tile) {
        for (ArrayList<Tile> group : gluedGroups) {
            if (group.contains(tile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el grupo de pegamento que contiene la ficha especificada.
     *
     * @param tile la ficha a buscar
     * @return el grupo de pegamento que contiene la ficha, o null si no está pegada
     */
    private static ArrayList<Tile> getGlueGroup(Tile tile) {
        for (ArrayList<Tile> group : gluedGroups) {
            if (group.contains(tile)) {
                return group;
            }
        }
        return null;
    }
    /**
     * Muestra el pegamento en la ficha en la posición especificada.
     *
     * @param tile la ficha a la que se añadirá el pegamento
     */
    public static void showGlue(Tile tile) {
        if (tile != null) {
            int x = tile.getXPosition() + 20;
            int y = tile.getYPosition() + 20;

            Rectangle tinyRec = new Rectangle(10, 10, x, y, "black");
            tinyRec.makeVisible();

            glueVisuals.put(tile, tinyRec); // Almacena la referencia del rectángulo
        } else {
            System.out.println("Ficha nula, no se puede mostrar el pegamento");
        }
    }

    /**
     * Oculta el pegamento de la ficha en la posición especificada.
     *
     * @param tile la ficha de la que se eliminará el pegamento
     */
    public static void hideGlue(Tile tile) {
        if (tile != null && glueVisuals.containsKey(tile)) {
            Rectangle tinyRec = glueVisuals.get(tile);
            tinyRec.makeInvisible();
            glueVisuals.remove(tile); // Elimina la referencia del rectángulo

            String originalColor = tile.getColor();
            tile.changeColor(originalColor);
            tile.makeVisible();
        } else {
            System.out.println("Ficha sin pegamento o fuera de los límites");
        }
    }
}
