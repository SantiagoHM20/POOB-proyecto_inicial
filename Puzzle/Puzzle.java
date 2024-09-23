import java.util.Scanner;
import java.util.ArrayList;

public class Puzzle {
    private int w;
    private int h;
    private char[][] Starting;
    private char[][] Ending;
    private boolean[][] glue;
    private Rectangle board;
    private ArrayList<Tile> tiles = new ArrayList<>();

    public Puzzle(int h, int w) {
        this.h = h;
        this.w = w;
        this.Starting = new char[h][w];
        this.Ending = new char[h][w];
        this.glue = new boolean[h][w];
    }

    public Puzzle(char[][] ending) {
        this.Ending = ending;
        this.h = ending.length;
        this.w = ending[0].length;
        this.Starting = new char[h][w];
        this.glue = new boolean[h][w];
    }

    public Puzzle(char[][] starting, char[][] ending) {
        this.Starting = starting;
        this.Ending = ending;
        this.h = starting.length;
        this.w = starting[0].length;
        this.glue = new boolean[h][w];
        initializeTiles();
    }

    private void initializeTiles() {
        board = new Rectangle(h * 50, w * 50, 0, 0, "black");
        board.makeVisible();
        tiles.clear(); // Limpia la lista de fichas antes de inicializar
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                if (Starting[row][column] != '.') {
                    String color = getColor(Starting[row][column]);
                    Tile tile = new Tile(50, 50, column * 50, row * 50, color, Starting[row][column]);
                    tiles.add(tile);
                }
            }
        }
    }

    private String getColor(char tile) {
        switch (tile) {
            case 'r':
                return "red";
            case 'b':
                return "blue";
            case 'y':
                return "yellow";
            case 'g':
                return "green";
            default:
                return "black";
        }
    }

    public void addTile(int row, int column, char color) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            String colorName = getColor(color);
            for (Tile tile : tiles) {
                if (tile.getXPosition() == column * 50 && tile.getYPosition() == row * 50) {
                    System.out.println("Posición ya ocupada.");
                    return;
                }
            }
            Tile newTile = new Tile(50, 50, column * 50, row * 50, colorName, color);
            newTile.makeVisible();
            tiles.add(newTile);
            Starting[row][column] = color;
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    public void deleteTile(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            Starting[row][column] = '.';
            for (int i = 0; i < tiles.size(); i++) {
                Tile tile = tiles.get(i);
                if (tile.getXPosition() == column * 50 && tile.getYPosition() == row * 50) {
                    tile.makeInvisible();
                    tiles.remove(i);
                    break;
                }
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    public void makeHole(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            // Verificar si ya hay un hueco en esa posición
            for (Tile tile : tiles) {
                if (tile.getXPosition() == column * 50 && tile.getYPosition() == row * 50 && "hole".equals(tile.getTypet())) {
                    System.out.println("Ya hay un hueco en esta posición.");
                    return;
                }
            }

            // Crear un hueco en la posición
            Tile hole = new Tile(50, 50, column * 50, row * 50, "white", "hole");
            hole.makeVisible();
            tiles.add(hole);
            Starting[row][column] = '.';  // Usamos un punto para indicar un hueco

            System.out.println("Hueco creado en (" + row + ", " + column + ").");
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    public void relocate(int[] from, int[] to) {
        int fromRow = from[0];
        int fromColumn = from[1];
        int toRow = to[0];
        int toColumn = to[1];

        if (fromRow >= 0 && fromRow < h && fromColumn >= 0 && fromColumn < w &&
            toRow >= 0 && toRow < h && toColumn >= 0 && toColumn < w) {

            char tile = Starting[fromRow][fromColumn];

            // Verificar si la posición destino es un hueco
            boolean isHole = false;
            for (Tile tileObject : tiles) {
                if (tileObject.getXPosition() == toColumn * 50 && tileObject.getYPosition() == toRow * 50 && "hole".equals(tileObject.getTypet())) {
                    isHole = true;
                    break;
                }
            }

            if (isHole) {
                // Si es un hueco, eliminar la ficha
                deleteTile(fromRow, fromColumn);
                System.out.println("La ficha cayó en un hueco y fue eliminada.");
            } else {
                // Mover la ficha si no es un hueco
                Starting[toRow][toColumn] = tile;
                Starting[fromRow][fromColumn] = '.';  // Usamos '.' para representar espacio vacío
                glue[toRow][toColumn] = glue[fromRow][fromColumn];
                glue[fromRow][fromColumn] = false;

                // Eliminar la ficha de la posición original
                for (int i = 0; i < tiles.size(); i++) {
                    Tile tileObject = tiles.get(i);
                    if (tileObject.getXPosition() == fromColumn * 50 && tileObject.getYPosition() == fromRow * 50) {
                        tileObject.makeInvisible();
                        tiles.remove(i);
                        break;
                    }
                }

                // Crear y mostrar la nueva ficha en la nueva posición
                Tile newTile = new Tile(50, 50, toColumn * 50, toRow * 50, getColor(tile), tile);
                tiles.add(newTile);
                newTile.makeVisible();
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }
    
    public void exchange() {
        // Crear una copia temporal de Ending
        char[][] tempEnding = new char[h][w];
        for (int i = 0; i < h; i++) {
            System.arraycopy(Ending[i], 0, tempEnding[i], 0, w);
        }
    
        // Intercambiar Starting con Ending
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                // Si hay una ficha en Starting, se debe eliminar de la copia de Ending
                if (Starting[row][column] != '.') {
                    // Reemplazar en Ending con un espacio vacío
                    tempEnding[row][column] = '.'; 
                } else {
                    // Si había una ficha en Ending, se mueve a Starting
                    tempEnding[row][column] = Starting[row][column];
                }
            }
        }
    
        // Actualizar Starting y Ending
        Starting = tempEnding; 
        Ending = tempEnding; 
    }
    public void tilt(char direction) {
        switch (direction) {
             case 'L':
                tiltLeft();
                    break;
            case 'R':
                tiltRight();
                    break;
            case 'U':
                tiltUp();
                    break;
            case 'D':
                tiltDown();
                    break;
            default:
                System.out.println("Dirección inválida");
        }
    }

    private void tiltLeft() {
        for (int row = 0; row < h; row++) {
            int targetColumn = 0; // Empezar desde la primera columna
            for (int column = 0; column < w; column++) { // Iterar de izquierda a derecha
                if (Starting[row][column] != '.') { // Si encontramos una ficha
                    // Verificar si la posición destino está vacía
                    if (Starting[row][targetColumn] == '.') {
                        relocate(new int[]{row, column}, new int[]{row, targetColumn});
                    } else {
                        // Verificar si la posición destino es un hueco
                        if (isHoleAt(row, targetColumn)) {
                            deleteTile(row, column);
                            System.out.println("La ficha cayó en un hueco y fue eliminada.");
                        }
                        // Avanzar a la siguiente columna destino
                        targetColumn++;
                    }
                }
            }
        }
    }
    
    private void tiltRight() {
        for (int row = 0; row < h; row++) {
            int targetColumn = w - 1; // Empezar desde la última columna
            for (int column = w - 1; column >= 0; column--) { // Iterar de derecha a izquierda
                if (Starting[row][column] != '.') { // Si encontramos una ficha
                    if (Starting[row][targetColumn] == '.') { // Verificar si la posición destino está vacía
                        // Mover la ficha a la posición vacía
                        relocate(new int[]{row, column}, new int[]{row, targetColumn});
                        targetColumn--; // Actualizar la columna destino
                    } else {
                        // Verificar si es un hueco
                        for (Tile tileObject : tiles) {
                            if (tileObject.getXPosition() == targetColumn * 50 && tileObject.getYPosition() == row * 50) {
                                if ("hole".equals(tileObject.getTypet())) {
                                    // Si encontramos un hueco, eliminar la ficha
                                    deleteTile(row, column);
                                    System.out.println("La ficha cayó en un hueco y fue eliminada.");
                                    break;
                                } else {
                                    // Si está ocupado por otra ficha, romper el bucle
                                    break;
                                }
                            }
                        }
                        targetColumn--; // Ajustar para la siguiente ficha
                    }
                }
            }
        }
    }
    
    private void tiltUp() {
        for (int column = 0; column < w; column++) {
            int targetRow = 0; // Empezar desde la primera fila
            for (int row = 0; row < h; row++) { // Iterar de arriba hacia abajo
                if (Starting[row][column] != '.') { // Si encontramos una ficha
                    if (Starting[targetRow][column] == '.') { // Verificar si la posición destino está vacía
                        // Mover la ficha a la posición vacía
                        relocate(new int[]{row, column}, new int[]{targetRow, column});
                        targetRow++; // Actualizar la fila destino
                    } else {
                        // Verificar si es un hueco
                        for (Tile tileObject : tiles) {
                            if (tileObject.getXPosition() == column * 50 && tileObject.getYPosition() == targetRow * 50) {
                                if ("hole".equals(tileObject.getTypet())) {
                                    // Si encontramos un hueco, eliminar la ficha
                                    deleteTile(row, column);
                                    System.out.println("La ficha cayó en un hueco y fue eliminada.");
                                    break;
                                } else {
                                    // Si está ocupado por otra ficha, romper el bucle
                                    break;
                                }
                            }
                        }
                        targetRow++; // Ajustar para la siguiente ficha
                    }
                }
            }
        }
    }
    
    private void tiltDown() {
        for (int column = 0; column < w; column++) {
            int targetRow = h - 1; // Empezar desde la última fila
            for (int row = h - 1; row >= 0; row--) { // Iterar de abajo hacia arriba
                if (Starting[row][column] != '.') { // Si encontramos una ficha
                    if (Starting[targetRow][column] == '.') { // Verificar si la posición destino está vacía
                        // Mover la ficha a la posición vacía
                        relocate(new int[]{row, column}, new int[]{targetRow, column});
                        targetRow--; // Actualizar la fila destino
                    } else {
                        // Verificar si es un hueco
                        for (Tile tileObject : tiles) {
                            if (tileObject.getXPosition() == column * 50 && tileObject.getYPosition() == targetRow * 50) {
                                if ("hole".equals(tileObject.getTypet())) {
                                    // Si encontramos un hueco, eliminar la ficha
                                    deleteTile(row, column);
                                    System.out.println("La ficha cayó en un hueco y fue eliminada.");
                                    break;
                                } else {
                                    // Si está ocupado por otra ficha, romper el bucle
                                    break;
                                }
                            }
                        }
                        targetRow--; // Ajustar para la siguiente ficha
                    }
                }
            }
        }
    }
    private boolean isHoleAt(int row, int column) {
        for (Tile tile : tiles) {
            if (tile.getXPosition() == column * 50 && tile.getYPosition() == row * 50) {
                return "hole".equals(tile.getTypet());
            }
        }
        return false; // No hay un hueco en esa posición
    }
    
    public int misPlacedTiles() {
        int misPlacedCount = 0;
    
        for (Tile tile : tiles) {
            int tileRow = tile.getYPosition() / 50; // Obtener la fila a partir de la posición Y
            int tileColumn = tile.getXPosition() / 50; // Obtener la columna a partir de la posición X
    
            // Comprobar si la posición actual en Starting es diferente de Ending
            if (Starting[tileRow][tileColumn] != Ending[tileRow][tileColumn]) {
                misPlacedCount++; 
            }
        }
    
        return misPlacedCount;
    }

    public boolean isGoal() {
        char[][] currentArrangement = actualArrangement();

        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                if (currentArrangement[row][column] != Ending[row][column]) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] actualArrangement() {
        return Starting;
    }

    public void makeVisible() {
        board.makeVisible();
        for (Tile tile : tiles) {
            tile.makeVisible();
        }
    }

    public void makeInvisible() {
        board.makeInvisible();
        for (Tile tile : tiles) {
            tile.makeInvisible();
        }
    }
   
}

     
    

   
