import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa el juego de rompecabezas.
 * 
 * Esta clase gestiona la lógica del juego, incluyendo el estado inicial y final
 * del tablero, así como las operaciones de movimiento y verificación de 
 * condiciones del juego.
 * 
 * @autor Artega-Hurtado
 */
public class Puzzle {
    private int w;
    private int h;
    private Tile[][] starting; 
    private Tile[][] ending;  
    private Rectangle board;
    private Rectangle finalBoard;
    private char[][] tablero_inicial;
    private char[][] tablero_final;
    private ArrayList<ArrayList<Tile>> glue;

/**
 * Constructor que inicializa un rompecabezas dado un alto y un ancho.
 * Crea las matrices de inicio y fin para las piezas del rompecabezas y las inicializa.
 *
 * @param h el alto del rompecabezas
 * @param w el ancho del rompecabezas
 */
public Puzzle(int h, int w) {
    this.h = h;
    this.w = w;
    this.starting = new Tile[h][w]; 
    this.ending = new Tile[h][w];
    this.glue = new ArrayList<>();
    initializeTiles();
    initializeTilesEnd();
}

/**
 * Constructor que inicializa un rompecabezas dado dos tableros, inicial y final.
 * Verifica que ambos tableros no sean nulos, luego copia sus valores y los convierte en tiles.
 *
 * @param tablero_inicial el tablero inicial del rompecabezas
 * @param tablero_final el tablero final del rompecabezas
 */
public Puzzle(char[][] tablero_inicial, char[][] tablero_final) {
    if (tablero_inicial == null || tablero_final == null) {
        throw new IllegalArgumentException("Los tableros inicial y final no pueden ser nulos");
    }
    this.h = tablero_inicial.length; 
    this.w = tablero_inicial[0].length;
    this.tablero_inicial = new char[h][w];
    this.tablero_final = new char[h][w];
    this.glue = new ArrayList<>();
    
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            this.tablero_inicial[row][column] = tablero_inicial[row][column];
            this.tablero_final[row][column] = tablero_final[row][column];
        }
    }
    this.starting = new Tile[h][w];
    this.ending = new Tile[h][w]; 
    initializeTiles_char();
    convertCharsToTiles(); 
    convertFinalCharsToTiles(); 
    makeVisible();
}

/**
 * Constructor que inicializa un rompecabezas dado solo un tablero final.
 * Verifica que el tablero final no sea nulo, inicializa el tablero inicial como vacío y copia el final.
 *
 * @param tablero_final el tablero final del rompecabezas
 */
public Puzzle(char[][] tablero_final){
    if (tablero_final == null) {
        throw new IllegalArgumentException("El tablero final no puede ser nulo");
    }    
    this.h = tablero_final.length;
    this.w = tablero_final[0].length;
    this.tablero_final = new char[h][w];
    this.tablero_inicial = new char[h][w];  
    this.glue = new ArrayList<>();
    
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            this.tablero_final[row][column] = tablero_final[row][column];
            this.tablero_inicial[row][column] = '.'; 
        }
    }

    this.starting = new Tile[h][w];
    this.ending = new Tile[h][w]; 

    initializeTiles_empty();  
    convertFinalCharsToTiles();  
    makeVisible();
}

/**
 * Inicializa las piezas del rompecabezas inicial, creando un rectángulo que representa el tablero.
 * Establece todas las posiciones de las piezas iniciales como nulas.
 */
private void initializeTiles() {
    board = new Rectangle(h * 50, w * 50, 0, 0, "black");
    board.makeVisible();
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            starting[row][column] = null; 
        }
    }
}

/**
 * Inicializa las piezas del rompecabezas final, creando un rectángulo que representa el tablero final.
 * Establece todas las posiciones de las piezas finales como nulas.
 */
private void initializeTilesEnd() {
    int spacing = 50;  
    int finalOffsetX = w * 50 + spacing; 
    finalBoard = new Rectangle(h * 50, w * 50, finalOffsetX, 0, "black");
    finalBoard.makeVisible();
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            ending[row][column] = null;
        }
    }
}

/**
 * Inicializa las piezas del rompecabezas inicial como vacías, creando el tablero.
 * Establece todas las posiciones de las piezas iniciales como nulas.
 */
private void initializeTiles_empty() {
    board = new Rectangle(h * 50, w * 50, 0, 0, "black");
    board.makeVisible();

    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            starting[row][column] = null; 
        }
    }

    int spacing = 50; 
    int finalOffsetX = w * 50 + spacing;  
    finalBoard = new Rectangle(h * 50, w * 50, finalOffsetX, 0, "black");
    finalBoard.makeVisible();
}

/**
 * Inicializa las piezas del rompecabezas inicial utilizando caracteres, creando el tablero.
 */
private void initializeTiles_char() {
    board = new Rectangle(h * 50, w * 50, 0, 0, "black");
    board.makeVisible();

    int spacing = 50;  
    int finalOffsetX = w * 50 + spacing; 
    finalBoard = new Rectangle(h * 50, w * 50, finalOffsetX, 0, "black");
    finalBoard.makeVisible();
}

/**
 * Obtiene el color correspondiente a un carácter de pieza del rompecabezas.
 *
 * @param tileChar el carácter que representa la pieza
 * @return el color asociado a la pieza
 */
private String getColorForTile(char tileChar) {
    switch (tileChar) {
        case 'r':
            return "red";
        case 'b':
            return "blue";
        case 'y':
            return "yellow";
        case 'g':
            return "green";
        default:
            return "gray";
    }
}
   /**
 * Convierte los caracteres del tablero inicial en objetos Tile.
 * Recorre cada posición del tablero y asigna un color basado en el carácter.
 */
private void convertCharsToTiles() {
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            char currentChar = tablero_inicial[row][column];
            if (currentChar == '.') {
                starting[row][column] = null;
            } else {
                String color = getColorForTile(currentChar); 
                starting[row][column] = new Tile(50, 50, column * 50, row * 50, color, currentChar);
            }
        }
    }
}

/**
 * Convierte los caracteres del tablero final en objetos Tile.
 * Recorre cada posición del tablero y asigna un color basado en el carácter, ajustando la posición en el tablero final.
 */
private void convertFinalCharsToTiles() {
    int spacing = 50;  
    int finalOffsetX = w * 50 + spacing;  

    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            char currentChar = tablero_final[row][column];
            if (currentChar == '.') {
                ending[row][column] = null;  
            } else {
                String color = getColorForTile(currentChar);  
                ending[row][column] = new Tile(50, 50, column * 50 + finalOffsetX, row * 50, color, currentChar);
            }
        }
    }
}

/**
 * Añade una ficha en la posición especificada del tablero inicial.
 * Verifica que la posición no esté ocupada y que esté dentro de los límites del tablero.
 *
 * @param row la fila donde se añadirá la ficha
 * @param column la columna donde se añadirá la ficha
 * @param tileChar el carácter que representa la ficha
 */
public void addTile(int row, int column, char tileChar) {
    if (row >= 0 && row < h && column >= 0 && column < w) {
        if (starting[row][column] != null) {
            System.out.println("Posición ya ocupada.");
            return;
        }
        String color = getColorForTile(tileChar);
        Tile tile = new Tile(50, 50, column * 50, row * 50, color, tileChar);
        tile.makeVisible();
        starting[row][column] = tile; 
    } else {
        System.out.println("Coordenadas fuera de los límites del tablero.");
    }
}

/**
 * Elimina una ficha en la posición especificada del tablero inicial.
 * Verifica que la posición esté dentro de los límites y que haya una ficha en esa posición.
 *
 * @param row la fila de la ficha a eliminar
 * @param column la columna de la ficha a eliminar
 */
public void deleteTile(int row, int column) {
    if (row >= 0 && row < h && column >= 0 && column < w) {
        if (starting[row][column] != null) {
            starting[row][column].makeInvisible(); 
            starting[row][column] = null; 
        } else {
            System.out.println("No hay ficha en la posición indicada.");
        }
    } else {
        System.out.println("Coordenadas fuera de los límites del tablero.");
    }
}

/**
 * Añade pegamento a la ficha en la posición especificada.
 * Verifica que la posición esté dentro de los límites y si la ficha ya está pegada.
 *
 * @param row la fila de la ficha a pegar
 * @param column la columna de la ficha a pegar
 */
public void addGlue(int row, int column) {
    if (0 <= row && row < starting.length && 0 <= column && column < starting[0].length) {
        if (isGlued(row, column)) {
            System.out.println("La ficha ya está pegada");
            return;
        }

        ArrayList<Tile> adjacents = new ArrayList<>();
        ArrayList<ArrayList<Tile>> mergeGroup = new ArrayList<>();
        ArrayList<Tile> finalGroup = new ArrayList<>();
        
        adjacents = getAdjacentTiles(row, column);
        
        finalGroup.add(starting[row][column]);
        
        for (Tile tile : adjacents) {
            if (isGlued(tile.getRow(), tile.getColumn())) {
                for (ArrayList<Tile> group : glue) {
                    if (group.contains(tile)) {
                        mergeGroup.add(group);
                        break;
                    }
                }
            } else {
                finalGroup.add(tile);
            }
        }
    
        for (ArrayList<Tile> group : mergeGroup) {
            finalGroup.addAll(group);
        }
    
        for (ArrayList<Tile> group : mergeGroup) {
            glue.remove(group);
        }
        glue.add(finalGroup);
    } else {
        System.out.println("La posición no es parte del tablero");
    }
}

/**
 * Obtiene las fichas adyacentes a la posición especificada.
 * Devuelve una lista de fichas que están adyacentes y visibles.
 *
 * @param row la fila de la ficha
 * @param column la columna de la ficha
 * @return una lista de fichas adyacentes
 */
private ArrayList<Tile> getAdjacentTiles(int row, int column) {
    ArrayList<Tile> adjacentTiles = new ArrayList<>();
    if (row > 0 && starting[row - 1][column] != null) { 
        adjacentTiles.add(starting[row - 1][column]);
    }
    if (row < starting.length - 1 && starting[row + 1][column] != null) {
        adjacentTiles.add(starting[row + 1][column]);
    }
    if (column > 0 && starting[row][column - 1] != null) { 
        adjacentTiles.add(starting[row][column - 1]);
    }
    if (column < starting[0].length - 1 && starting[row][column + 1] != null) { 
        adjacentTiles.add(starting[row][column + 1]);
    }
    return adjacentTiles;
}

/**
 * Elimina el pegamento de la ficha en la posición especificada.
 * Verifica que la posición esté dentro de los límites y que la ficha esté pegada.
 *
 * @param row la fila de la ficha a deshacer el pegado
 * @param column la columna de la ficha a deshacer el pegado
 */
public void deleteGlue(int row, int column) {
    if (0 <= row && row < starting.length && 0 <= column && column < starting[0].length) {
        if (!isGlued(row, column)) {
            System.out.println("La ficha no está pegada");
            return;
        }

        ArrayList<Tile> adjacentTiles;
        ArrayList<ArrayList<Tile>> newGlues = new ArrayList<>();
        ArrayList<Tile> originalGlue;

        adjacentTiles = getAdjacentTiles(row, column);
        originalGlue = getGlue(starting[row][column]);

        originalGlue.remove(starting[row][column]);
        hideGlue(row, column);
        
    } else {
        System.out.println("La posición no es parte del tablero");
    }
}

/**
 * Verifica si la ficha en la posición especificada está pegada.
 *
 * @param row la fila de la ficha
 * @param column la columna de la ficha
 * @return true si la ficha está pegada, false en caso contrario
 */
private boolean isGlued(int row, int column) {
    if (row >= 0 && row < h && column >= 0 && column < w) {
        Tile tile = starting[row][column];
        return tile != null && tile.isGlued();
    }
    return false;
}

/**
 * Muestra el pegamento en la ficha en la posición especificada.
 *
 * @param row la fila de la ficha
 * @param column la columna de la ficha
 */
private void showGlue(int row, int column) {
    if (row >= 0 && row < starting.length && column >= 0 && column < starting[0].length) {
        Tile currentTile = starting[row][column];
        if (currentTile != null) {
            int x = currentTile.getXPosition() + 20; 
            int y = currentTile.getYPosition() + 20;

            Rectangle tinyRec = new Rectangle(10, 10, x, y, "black");
            tinyRec.makeVisible(); 
        }
    } else {
        System.out.println("Fuera de los límites del tablero");
    }
}

/**
 * Oculta el pegamento de la ficha en la posición especificada.
 *
 * @param row la fila de la ficha
 * @param column la columna de la ficha
 */
private void hideGlue(int row, int column) {
    if (row >= 0 && row < starting.length && column >= 0 && column < starting[0].length) {
        Tile currentTile = starting[row][column];
        if (currentTile != null) {
            String originalColor = currentTile.getColor(); 
            currentTile.changeColor(originalColor); 
            currentTile.makeVisible();            
        }
    } else {
        System.out.println("Fuera de los límites del tablero");
    }
}
    
   /**
 * Crea un hueco en la posición especificada del tablero inicial.
 * Verifica que la posición esté dentro de los límites y que no haya ya un hueco.
 *
 * @param row la fila donde se creará el hueco
 * @param column la columna donde se creará el hueco
 */
public void makeHole(int row, int column) {
    if (row >= 0 && row < h && column >= 0 && column < w) {
        Tile existingTile = starting[row][column];
        if (existingTile != null && "hole".equals(existingTile.getTypet())) {
            System.out.println("Ya hay un hueco en esta posición.");
            return;
        }

        String color = "white"; 
        Tile holeTile = new Tile(50, 50, column * 50, row * 50, color, "hole");
        holeTile.makeVisible(); 
        starting[row][column] = holeTile; 

        System.out.println("Hueco creado en (" + row + ", " + column + ").");
    } else {
        System.out.println("Coordenadas fuera de los límites del tablero.");
    }
}

/**
 * Reubica una ficha de la posición de origen a la posición de destino.
 * Verifica que ambas posiciones estén dentro de los límites y que haya una ficha en la posición de origen.
 *
 * @param from arreglo que contiene la posición de origen [fila, columna]
 * @param to arreglo que contiene la posición de destino [fila, columna]
 */
public void relocate(int[] from, int[] to) {
    int fromRow = from[0];
    int fromColumn = from[1];
    int toRow = to[0];
    int toColumn = to[1];

    if (fromRow >= 0 && fromRow < h && fromColumn >= 0 && fromColumn < w &&
        toRow >= 0 && toRow < h && toColumn >= 0 && toColumn < w) {   
        if (starting[fromRow][fromColumn] == null) {
            System.out.println("No hay ficha en la posición de origen.");
            return; 
        }
        if (starting[fromRow][fromColumn].getTypet().equals("hole")) {
            return;
        }
        if (starting[toRow][toColumn] == null) {
            Tile tile = starting[fromRow][fromColumn];
            starting[toRow][toColumn] = tile;
            starting[fromRow][fromColumn] = null;
            tile.setPosition(toColumn * 50, toRow * 50);
        } else if (starting[toRow][toColumn].getTypet().equals("hole")) {
            deleteTile(fromRow, fromColumn); 
        }
    } else {
        System.out.println("Coordenadas fuera de los límites del tablero.");
    }
}

/**
 * Reubica una ficha en una copia del tablero, sin afectar el original.
 *
 * @param from arreglo que contiene la posición de origen [fila, columna]
 * @param to arreglo que contiene la posición de destino [fila, columna]
 * @param startingCopy copia del tablero inicial
 */
private void relocate(int[] from, int[] to, Tile[][] startingCopy) {
    int fromRow = from[0];
    int fromColumn = from[1];
    int toRow = to[0];
    int toColumn = to[1];

    if (fromRow >= 0 && fromRow < h && fromColumn >= 0 && fromColumn < w &&
        toRow >= 0 && toRow < h && toColumn >= 0 && toColumn < w) {   
        if (startingCopy[fromRow][fromColumn] == null) {
            System.out.println("No hay ficha en la posición de origen en la copia.");
            return; 
        }
        if (startingCopy[fromRow][fromColumn].getTypet().equals("hole")) {
            return;
        }
        if (startingCopy[toRow][toColumn] == null) {
            Tile tile = startingCopy[fromRow][fromColumn];
            startingCopy[toRow][toColumn] = tile; 
            startingCopy[fromRow][fromColumn] = null; 
            tile.setPosition(toColumn * 50, toRow * 50); 
        } else if (startingCopy[toRow][toColumn].getTypet().equals("hole")) {
            deleteTile(fromRow, fromColumn); 
        }
    } else {
        System.out.println("Coordenadas fuera de los límites del tablero.");
    }
}

/**
 * Intercambia las fichas entre los tableros inicial y final.
 * Mueve cada ficha del tablero final al inicial y viceversa.
 */
public void exchange() {
    int spacing = 50; 
    int finalOffsetX = w * 50 + spacing;  

    Tile[][] tempStarting = new Tile[h][w]; 

    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            tempStarting[row][column] = starting[row][column];

            if (starting[row][column] != null) {
                starting[row][column].setPosition(column * 50 + finalOffsetX, row * 50);
            }
            if (ending[row][column] != null) {
                ending[row][column].setPosition(column * 50, row * 50);
            }

            starting[row][column] = ending[row][column];
            ending[row][column] = tempStarting[row][column];
        }
    }
}

/**
 * Realiza un "tilt" inteligente usando la función misPlacedTiles().
 * Determina la dirección que minimiza la cantidad de fichas fuera de lugar.
 */
public void tilt() {
    Tile[][] copy = gameCopy();
    tiltSimulator('l', copy);
    int leftDiff = misPlacedTiles(copy);
    copy = gameCopy();
    tiltSimulator('r', copy);
    int rightDiff = misPlacedTiles(copy);
    copy = gameCopy();
    tiltSimulator('u', copy);
    int upDiff = misPlacedTiles(copy);
    copy = gameCopy();
    tiltSimulator('d', copy);
    int downDiff = misPlacedTiles(copy);
    
    int minDiff = Math.min(Math.min(leftDiff, rightDiff), Math.min(upDiff, downDiff));
    if (minDiff == leftDiff) {
        tilt('l');
    } else if (minDiff == rightDiff) {
        tilt('r');
    } else if (minDiff == upDiff) {
        tilt('u');
    } else if (minDiff == downDiff) {
        tilt('d');
    }
}

/**
 * Simula un tilt en la dirección especificada en una copia del tablero.
 *
 * @param direction la dirección en la que se realizará el tilt ('l', 'r', 'u', 'd')
 * @param startingCopy copia del tablero inicial
 */
private void tiltSimulator(char direction, Tile[][] startingCopy) {
    switch (direction) {
        case 'l':
            tiltLeftSimulator(startingCopy);
            break;
        case 'r':
            tiltRightSimulator(startingCopy);
            break;
        case 'u':
            tiltUpSimulator(startingCopy);
            break;
        case 'd':
            tiltDownSimulator(startingCopy);
            break;
        default:
            System.out.println("Dirección inválida");
    }
}
    
    /**
 * Simula la inclinación hacia la izquierda en el tablero.
 * Mueve las fichas a la izquierda, fusionando las que se encuentren en la misma fila.
 *
 * @param startingCopy la copia del tablero inicial
 */
private void tiltLeftSimulator(Tile[][] startingCopy) {
    for (int row = 0; row < h; row++) {
        boolean moved;
        do {
            moved = false;
            for (int column = 1; column < w; column++) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row][column - 1] == null) {
                        relocate(new int[]{row, column}, new int[]{row, column - 1}, startingCopy); 
                        moved = true;
                    } else if (isHoleAt(row, column - 1)) {
                        deleteTile(row, column);
                        moved = true; 
                        break;
                    }
                }
            }
            moved = moved || canMoveLeftSimulator(row, startingCopy);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse a la izquierda en la fila especificada.
 *
 * @param row la fila que se está evaluando
 * @param startingCopy la copia del tablero inicial
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveLeftSimulator(int row, Tile[][] startingCopy) {
    for (int column = 1; column < w; column++) {
        if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha") && startingCopy[row][column - 1] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Simula la inclinación hacia la derecha en el tablero.
 * Mueve las fichas a la derecha, fusionando las que se encuentren en la misma fila.
 *
 * @param startingCopy la copia del tablero inicial
 */
private void tiltRightSimulator(Tile[][] startingCopy) {
    for (int row = 0; row < h; row++) {
        boolean moved;
        do {
            moved = false;
            for (int column = w - 2; column >= 0; column--) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row][column + 1] == null) {
                        relocate(new int[]{row, column}, new int[]{row, column + 1}, startingCopy); 
                        moved = true;
                    } else if (isHoleAt(row, column + 1)) {
                        deleteTile(row, column);
                        moved = true; 
                        break;
                    }
                }
            }
            moved = moved || canMoveRightSimulator(row, startingCopy);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse a la derecha en la fila especificada.
 *
 * @param row la fila que se está evaluando
 * @param startingCopy la copia del tablero inicial
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveRightSimulator(int row, Tile[][] startingCopy) {
    for (int column = w - 2; column >= 0; column--) {
        if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha") && startingCopy[row][column + 1] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Simula la inclinación hacia arriba en el tablero.
 * Mueve las fichas hacia arriba, fusionando las que se encuentren en la misma columna.
 *
 * @param startingCopy la copia del tablero inicial
 */
private void tiltUpSimulator(Tile[][] startingCopy) {
    for (int column = 0; column < w; column++) {
        boolean moved;
        do {
            moved = false;
            for (int row = 1; row < h; row++) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row - 1][column] == null) {
                        relocate(new int[]{row, column}, new int[]{row - 1, column}, startingCopy);
                        moved = true;
                    } else if (isHoleAt(row - 1, column)) {
                        deleteTile(row, column);
                        moved = true; 
                        break;
                    }
                }
            }
            moved = moved || canMoveUpSimulator(column, startingCopy);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse hacia arriba en la columna especificada.
 *
 * @param column la columna que se está evaluando
 * @param startingCopy la copia del tablero inicial
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveUpSimulator(int column, Tile[][] startingCopy) {
    for (int row = 1; row < h; row++) {
        if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha") && startingCopy[row - 1][column] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Simula la inclinación hacia abajo en el tablero.
 * Mueve las fichas hacia abajo, fusionando las que se encuentren en la misma columna.
 *
 * @param startingCopy la copia del tablero inicial
 */
private void tiltDownSimulator(Tile[][] startingCopy) {
    for (int column = 0; column < w; column++) {
        boolean moved;
        do {
            moved = false;
            for (int row = h - 2; row >= 0; row--) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row + 1][column] == null) {
                        relocate(new int[]{row, column}, new int[]{row + 1, column}, startingCopy); 
                        moved = true;
                    } else if (isHoleAt(row + 1, column)) {
                        deleteTile(row, column);
                        moved = true; 
                        break;
                    }
                }
            }
            moved = moved || canMoveDownSimulator(column, startingCopy);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse hacia abajo en la columna especificada.
 *
 * @param column la columna que se está evaluando
 * @param startingCopy la copia del tablero inicial
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveDownSimulator(int column, Tile[][] startingCopy) {
    for (int row = h - 2; row >= 0; row--) {
        if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha") && startingCopy[row + 1][column] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Realiza una inclinación en la dirección especificada.
 * Llama a la función de inclinación correspondiente según la dirección.
 *
 * @param direction la dirección en la que se realizará la inclinación ('l', 'r', 'u', 'd')
 */
public void tilt(char direction) {
    switch (direction) {
        case 'l':
            tiltLeft();
            break;
        case 'r':
            tiltRight();
            break;
        case 'u':
            tiltUp();
            break;
        case 'd':
            tiltDown();
            break;
        default:
            System.out.println("Dirección inválida");
    }
}
    
       
    /**
 * Realiza la inclinación hacia la izquierda en el tablero.
 * Mueve las fichas a la izquierda y elimina las que caen en un hueco.
 */
private void tiltLeft() {
    for (int row = 0; row < h; row++) {
        boolean moved;
        do {
            moved = false;
            for (int column = 1; column < w; column++) { 
                if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha")) { 
                    if (starting[row][column - 1] == null) { 
                        relocate(new int[]{row, column}, new int[]{row, column - 1}); 
                        moved = true; 
                    } else if (isHoleAt(row, column - 1)) { 
                        deleteTile(row, column); 
                        break; 
                    }
                }
            }
            moved = moved || canMoveLeft(row);
        } while (moved); 
    }
}

/**
 * Verifica si hay fichas que pueden moverse a la izquierda en la fila especificada.
 *
 * @param row la fila que se está evaluando
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveLeft(int row) {
    for (int column = 1; column < w; column++) {
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row][column - 1] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Realiza la inclinación hacia la derecha en el tablero.
 * Mueve las fichas a la derecha y elimina las que caen en un hueco.
 */
private void tiltRight() {
    for (int row = 0; row < h; row++) {
        boolean moved;
        do {
            moved = false;
            for (int column = w - 2; column >= 0; column--) { 
                if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha")) {
                    if (starting[row][column + 1] == null) {
                        relocate(new int[]{row, column}, new int[]{row, column + 1});
                        moved = true;
                    } else if (isHoleAt(row, column + 1)) {
                        deleteTile(row, column);
                        break;
                    }
                }
            }
            moved = moved || canMoveRight(row);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse a la derecha en la fila especificada.
 *
 * @param row la fila que se está evaluando
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveRight(int row) {
    for (int column = w - 2; column >= 0; column--) {
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row][column + 1] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Realiza la inclinación hacia arriba en el tablero.
 * Mueve las fichas hacia arriba y elimina las que caen en un hueco.
 */
private void tiltUp() {
    for (int column = 0; column < w; column++) {
        boolean moved;
        do {
            moved = false;
            for (int row = 1; row < h; row++) { 
                if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha")) {
                    if (starting[row - 1][column] == null) { 
                        relocate(new int[]{row, column}, new int[]{row - 1, column});
                        moved = true;
                    } else if (isHoleAt(row - 1, column)) {
                        deleteTile(row, column);
                        break;
                    }
                }
            }
            moved = moved || canMoveUp(column);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse hacia arriba en la columna especificada.
 *
 * @param column la columna que se está evaluando
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveUp(int column) {
    for (int row = 1; row < h; row++) {
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row - 1][column] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Realiza la inclinación hacia abajo en el tablero.
 * Mueve las fichas hacia abajo y elimina las que caen en un hueco.
 */
private void tiltDown() {
    for (int column = 0; column < w; column++) {
        boolean moved;
        do {
            moved = false;
            for (int row = h - 2; row >= 0; row--) { 
                if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha")) {
                    if (starting[row + 1][column] == null) { 
                        relocate(new int[]{row, column}, new int[]{row + 1, column});
                        moved = true;
                    } else if (isHoleAt(row + 1, column)) {
                        deleteTile(row, column);
                        break;
                    }
                }
            }
            moved = moved || canMoveDown(column);
        } while (moved);
    }
}

/**
 * Verifica si hay fichas que pueden moverse hacia abajo en la columna especificada.
 *
 * @param column la columna que se está evaluando
 * @return true si hay fichas que pueden moverse, false en caso contrario
 */
private boolean canMoveDown(int column) {
    for (int row = h - 2; row >= 0; row--) {
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row + 1][column] == null) {
            return true; 
        }
    }
    return false;
}

/**
 * Verifica si hay un hueco en la posición especificada.
 *
 * @param row la fila de la posición
 * @param column la columna de la posición
 * @return true si hay un hueco, false en caso contrario
 */
private boolean isHoleAt(int row, int column) {
    return row >= 0 && row < h && column >= 0 && column < w && starting[row][column] != null && "hole".equals(starting[row][column].getTypet());
}

/**
 * Cuenta el número de fichas mal colocadas en el tablero.
 *
 * @return el número de fichas mal colocadas
 */
public int misPlacedTiles() {
    int misPlacedCount = 0;

    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {      
            if (ending[row][column] != null) {
                if (starting[row][column] == null || !starting[row][column].getColor().equals(ending[row][column].getColor())) {
                    misPlacedCount++; 
                }
            }
        }
    }

    return misPlacedCount;
}

/**
 * Cuenta el número de fichas mal colocadas en una copia del tablero.
 *
 * @param startingCopy la copia del tablero inicial
 * @return el número de fichas mal colocadas
 */
private int misPlacedTiles(Tile[][] startingCopy) {
    int misPlacedCount = 0;

    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {     
            if ((startingCopy[row][column] != null && ending[row][column] == null) || 
                (ending[row][column] != null && startingCopy[row][column] == null) ||
                (startingCopy[row][column] != null && !startingCopy[row][column].getColor().equals(ending[row][column].getColor()))) {
                misPlacedCount++;
            }
        }
    }

    return misPlacedCount;
}
    
        
/**
 * Obtiene una lista de fichas fijas que no se pueden mover.
 *
 * @return una lista de coordenadas de las fichas fijas
 */
public List<int[]> fixedTiles() {
    List<int[]> allImmovableTiles = new ArrayList<>();
    char[] directions = {'L', 'R', 'U', 'D'};


    for (char direction : directions) {
        Tile[][] copyboard = gameCopy();
        tiltSimulator(direction, copyboard); 

        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                Tile original = starting[row][column];
                Tile current = copyboard[row][column];

                if (original != null && current != null && original.equals(current)) {
                    allImmovableTiles.add(new int[]{row, column});
                }
            }
        }
    }


    Map<String, Integer> coordinateCount = new HashMap<>();
    for (int[] coords : allImmovableTiles) {
        String key = coords[0] + "," + coords[1];  
        coordinateCount.put(key, coordinateCount.getOrDefault(key, 0) + 1);
    }

 
    List<int[]> finalImmovableTiles = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : coordinateCount.entrySet()) {
        if (entry.getValue() == 4) {
            String[] parts = entry.getKey().split(",");
            finalImmovableTiles.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }
    }

    return finalImmovableTiles;
}

/**
 * Verifica si el tablero actual es el objetivo deseado.
 *
 * @return true si el tablero actual coincide con el objetivo, false en caso contrario
 */
public boolean isGoal() {
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            Tile currentTile = starting[row][column];
            Tile targetTile = ending[row][column];

            if ((currentTile == null && targetTile != null) || 
                (currentTile != null && targetTile == null) ||
                (currentTile != null && targetTile != null && 
                    (currentTile.getType() != targetTile.getType() || 
                    !currentTile.getTypet().equals(targetTile.getTypet())))) {
                return false; 
            }
        }
    }
    return true; 
}
/**
 * Verifica si ambos tableros son iguales
 * retorna true si lo son, false si no
 */
public boolean ok(){
    if(starting == ending){
    return true;
    }
    return false;
}


/**
 * Finaliza la máquina virtual y cierra el programa.
 */
public void finish() {
    System.exit(0);
}

/**
 * Devuelve la disposición actual de las fichas.
 *
 * @return el tablero actual
 */
public Tile[][] actualArrangement() {
    return starting; 
}

/**
 * Hace visibles el tablero y todas las fichas.
 */
public void makeVisible() {
    board.makeVisible();
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            if (starting[row][column] != null) {
                starting[row][column].makeVisible();
            }
        }
    }
    finalBoard.makeVisible();
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            if (ending[row][column] != null) {
                ending[row][column].makeVisible();
            }
        }
    }
}

/**
 * Hace invisibles el tablero y todas las fichas.
 */
public void makeInvisible() {
    board.makeInvisible();
    for (int row = 0; row < h; row++) { 
        for (int column = 0; column < w; column++) {
            if (starting[row][column] != null) {
                starting[row][column].makeInvisible(); 
            }
        }
    }

    finalBoard.makeInvisible();
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            if (ending[row][column] != null) {
                ending[row][column].makeInvisible();
            }
        }
    }
}

/**
 * Crea una copia del tablero actual.
 *
 * @return una copia del tablero
 */
private Tile[][] gameCopy() {
    Tile[][] copy = new Tile[starting.length][starting[0].length];
    for (int h = 0; h < starting.length; h++) {
        for (int w = 0; w < starting[0].length; w++) {
            if (starting[h][w] != null) {
                copy[h][w] = new Tile(
                    starting[h][w].getWidth(), 
                    starting[h][w].getHeight(), 
                    starting[h][w].getXPosition(), 
                    starting[h][w].getYPosition(), 
                    starting[h][w].getColor(), 
                    starting[h][w].getType()    
                );
            }
        }
    }
    return copy;
}

/**
 * Obtiene la lista de fichas que están unidas a la ficha especificada.
 *
 * @param tile la ficha de la cual se desea obtener las uniones
 * @return la lista de fichas unidas o null si no hay ninguna
 */
private ArrayList<Tile> getGlue(Tile tile) {
    for (ArrayList<Tile> glueTile : glue) {
        for (Tile tile2 : glueTile) {
            if (tile.equals(tile2)) {
                return glueTile;
            }
        }
    }
    return null;
}
}