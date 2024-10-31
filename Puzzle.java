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
    private char[][] tablero_inicial;
    private char[][] tablero_final;
    private ArrayList<ArrayList<Tile>> glue;
    private Rectangle initialBoard; // Tablero inicial
    private Rectangle finalBoard;   

    public Puzzle(int h, int w) {
        this.h = h;
        this.w = w;
        this.starting = new Tile[h][w]; 
        this.ending = new Tile[h][w];
        this.glue = new ArrayList<>();
        PuzzleBoardInitializer.initializeStartingBoard(this.starting, h, w);
        PuzzleBoardInitializer.initializeEndingBoard(this.ending, h, w);
    }

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

    // No llamamos a initializeStartingBoard, ya que ya tenemos las fichas
    PuzzleBoardInitializer.convertCharsToStartingTiles(this.tablero_inicial, this.starting, h, w);
    PuzzleBoardInitializer.convertCharsToEndingTiles(this.tablero_final, this.ending, h, w);
    makeVisible();
}

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

        PuzzleBoardInitializer.initializeStartingBoard(this.starting, h, w);
        PuzzleBoardInitializer.convertCharsToEndingTiles(this.tablero_final, this.ending, h, w);
        makeVisible();
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
        String color = PuzzleBoardInitializer.getColorForTile(tileChar);
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
            if(starting[row][column].getTypet().equals("Fixed")){
                System.out.println("La ficha es de tipo Fixed y no puede ser eliminada");
                return;
            }
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
 * Verifica que la posición esté dentro de los límites y añade pegamento a las fichas adyacentes.
 *
 * @param row la fila de la ficha a pegar
 * @param col la columna de la ficha a pegar
 */
public void addGlue(int row, int col) {
    Tile tile = starting[row][col]; // Usar starting para acceder a la ficha
    ArrayList<Tile> adjacentTiles = getAdjacentTiles(row, col);
    
    for (Tile adjacentTile : adjacentTiles) {
        Glue.addGlue(tile, adjacentTiles); // Pasar una ficha y una ficha adyacente
    }
}

/**
 * Elimina el pegamento de la ficha en la posición especificada.
 * Verifica que la posición esté dentro de los límites y elimina el pegamento de las fichas adyacentes.
 *
 * @param row la fila de la ficha a deshacer el pegado
 * @param col la columna de la ficha a deshacer el pegado
 */
public void deleteGlue(int row, int col) {
    Tile tile = starting[row][col]; 
    ArrayList<Tile> adjacentTiles = getAdjacentTiles(row, col);
    
    for (Tile adjacentTile : adjacentTiles) {
        Glue.deleteGlue(tile, adjacentTiles); // Pasar una ficha y una ficha adyacente
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
        Hole holeTile = new Hole(50, 50, column * 50, row * 50);
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
        if(starting[fromRow][fromColumn].getTypet().equals("Fixed")){
                System.out.println("La ficha es de tipo Fixed y no puede ser eliminada");
                return;
            }
        if (starting[fromRow][fromColumn].getTypet().equals("hole")) {
            return;
        }
        if(starting[toRow][toColumn] == null && starting[fromRow][fromColumn].getTypet().equals("Flying") && starting[toRow][toColumn].getTypet().equals("hole")){
            Tile tile = starting[fromRow][fromColumn];
            starting[toRow][toColumn] = tile;
            starting[fromRow][fromColumn] = null;
            tile.setPosition(toColumn * 50, toRow * 50);
        }
        if (starting[toRow][toColumn] == null) {
            Tile tile = starting[fromRow][fromColumn];
            starting[toRow][toColumn] = tile;
            starting[fromRow][fromColumn] = null;
            tile.setPosition(toColumn * 50, toRow * 50);
        } 
        
        else if (starting[toRow][toColumn].getTypet().equals("hole")) {
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
    Simulator simulator = new Simulator(this);
    Tile[][] copy = gameCopy();
    simulator.tiltSimulator('l', copy, this);
    int leftDiff = misPlacedTiles(copy);
    copy = gameCopy();
    simulator.tiltSimulator('r', copy, this);
    int rightDiff = misPlacedTiles(copy);
    copy = gameCopy();
    simulator.tiltSimulator('u', copy, this);
    int upDiff = misPlacedTiles(copy);
    copy = gameCopy();
    simulator.tiltSimulator('d', copy, this);
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
                if (starting[row][column] != null && starting[row][column].getTypet().equals("Rough")) {
                    break;
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
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row][column - 1] == null && starting[row][column].getTypet()!="Rough" && starting[row][column].getTypet()!="Fixed") {
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
                if (starting[row][column] != null && starting[row][column].getTypet().equals("Rough")) {
                    break;
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
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row][column + 1] == null && starting[row][column].getTypet()!="Rough" && starting[row][column].getTypet()!="Fixed") {
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
                if (starting[row][column] != null && starting[row][column].getTypet().equals("Rough")) {
                    break;
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
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row - 1][column] == null && starting[row][column].getTypet()!="Rough" && starting[row][column].getTypet()!="Fixed") {
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
                if (starting[row][column] != null && starting[row][column].getTypet().equals("Rough")) {
                    break;
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
        if (starting[row][column] != null && starting[row][column].getTypet().equals("ficha") && starting[row + 1][column] == null && starting[row][column].getTypet()!="Rough" && starting[row][column].getTypet()!="Fixed") {
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
    Simulator simulator = new Simulator(this);
    List<int[]> allImmovableTiles = new ArrayList<>();
    char[] directions = {'L', 'R', 'U', 'D'};


    for (char direction : directions) {
        Tile[][] copyboard = gameCopy();
        simulator.tiltSimulator(direction, copyboard, this); 

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
 * Hace visibles todas las fichas del tablero inicial y final.
 */
public void makeVisible() {
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            if (starting[row][column] != null) {
                starting[row][column].makeVisible();
            }
        }
    }
    
    for (int row = 0; row < h; row++) {
        for (int column = 0; column < w; column++) {
            if (ending[row][column] != null) {
                ending[row][column].makeVisible();
            }
        }
    }
}

/**
 * Hace invisibles todas las fichas del tablero inicial y final.
 */
public void makeInvisible() {
    for (int row = 0; row < h; row++) { 
        for (int column = 0; column < w; column++) {
            if (starting[row][column] != null) {
                starting[row][column].makeInvisible(); 
            }
        }
    }

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
Tile[][] getStarting(){
    return starting;
}
}