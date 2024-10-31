public class Simulator {
    private Tile[][] startingCopy;
    private int h; // Altura del tablero
    private int w; // Ancho del tablero
    private Puzzle puzzle; // Instancia de Puzzle

    public Simulator(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.startingCopy = gameCopy(puzzle); // Crea la copia del tablero al inicializar
        this.h = startingCopy.length;
        this.w = startingCopy[0].length;
    }
    /**
 * Reubica una ficha en una copia del tablero, sin afectar el original.
 *
 * @param from arreglo que contiene la posición de origen [fila, columna]
 * @param to arreglo que contiene la posición de destino [fila, columna]
 * @param startingCopy copia del tablero inicial
 */
private void relocate(int[] from, int[] to, Tile[][] startingCopy, Puzzle puzzle) {
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
            puzzle.deleteTile(fromRow, fromColumn); 
        }
    } else {
        System.out.println("Coordenadas fuera de los límites del tablero.");
    }
}
/**
 * Simula un tilt en la dirección especificada en una copia del tablero.
 *
 * @param direction la dirección en la que se realizará el tilt ('l', 'r', 'u', 'd')
 * @param startingCopy copia del tablero inicial
 */
public void tiltSimulator(char direction, Tile[][] startingCopy, Puzzle puzzle) {
    switch (direction) {
        case 'l':
            tiltLeftSimulator(startingCopy, puzzle);
            break;
        case 'r':
            tiltRightSimulator(startingCopy, puzzle);
            break;
        case 'u':
            tiltUpSimulator(startingCopy, puzzle);
            break;
        case 'd':
            tiltDownSimulator(startingCopy, puzzle);
            break;
        default:
            System.out.println("Dirección inválida");
    }
}


    private void tiltLeftSimulator(Tile[][] startingCopy, Puzzle puzzle) {
        for (int row = 0; row < h; row++) {
            boolean moved;
            do {
                moved = false;
                for (int column = 1; column < w; column++) {
                    if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                        if (startingCopy[row][column - 1] == null) {
                            relocate(new int[]{row, column}, new int[]{row, column - 1},startingCopy, puzzle);
                            moved = true;
                        } else if (isHoleAt(row, column - 1, puzzle)) {
                            puzzle.deleteTile(row, column);
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
private void tiltRightSimulator(Tile[][] startingCopy, Puzzle puzzle) {
    for (int row = 0; row < h; row++) {
        boolean moved;
        do {
            moved = false;
            for (int column = w - 2; column >= 0; column--) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row][column + 1] == null) {
                        relocate(new int[]{row, column}, new int[]{row, column + 1}, startingCopy, puzzle); 
                        moved = true;
                    } else if (isHoleAt(row, column + 1, puzzle)) {
                        puzzle.deleteTile(row, column);
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
private void tiltUpSimulator(Tile[][] startingCopy, Puzzle puzzle) {
    for (int column = 0; column < w; column++) {
        boolean moved;
        do {
            moved = false;
            for (int row = 1; row < h; row++) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row - 1][column] == null) {
                        relocate(new int[]{row, column}, new int[]{row - 1, column}, startingCopy, puzzle);
                        moved = true;
                    } else if (isHoleAt(row - 1, column, puzzle)) {
                        puzzle.deleteTile(row, column);
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
private void tiltDownSimulator(Tile[][] startingCopy, Puzzle puzzle) {
    for (int column = 0; column < w; column++) {
        boolean moved;
        do {
            moved = false;
            for (int row = h - 2; row >= 0; row--) {
                if (startingCopy[row][column] != null && startingCopy[row][column].getTypet().equals("ficha")) {
                    if (startingCopy[row + 1][column] == null) {
                        relocate(new int[]{row, column}, new int[]{row + 1, column}, startingCopy, puzzle); 
                        moved = true;
                    } else if (isHoleAt(row + 1, column, puzzle)) {
                        puzzle.deleteTile(row, column);
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
 * Crea una copia del tablero actual.
 *
 * @return una copia del tablero
 */
private static Tile[][] gameCopy(Puzzle puzzle) {
    Tile[][] starting = puzzle.getStarting();
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
 * Verifica si hay un hueco en la posición especificada.Simulador
 *
 * @param row la fila de la posición
 * @param column la columna de la posición
 * @return true si hay un hueco, false en caso contrario
 */
private boolean isHoleAt(int row, int column, Puzzle puzzle) {
    return row >= 0 && row < h && column >= 0 && column < w && gameCopy(puzzle)[row][column] != null && "hole".equals(gameCopy(puzzle)[row][column].getTypet());
}


}