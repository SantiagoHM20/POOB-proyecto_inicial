public class Puzzle {
    private Tile[][] board;
    private Tile[][] starting;
    private Tile[][] ending;
    private int height;
    private int width;

    // Constructor que crea el tablero con tamaño fijo
    public Puzzle(int height, int width) {
        this.height = height;
        this.width = width;
        this.board = new Tile[height][width]; // Inicializa el tablero
        this.starting = new Tile[height][width]; // Inicializa el estado inicial
        this.ending = new Tile[height][width]; // Inicializa el estado final
    }

    // Constructor que inicializa solo el estado final
    public Puzzle(Tile[][] ending) {
        this.ending = ending;
        this.height = ending.length;
        this.width = ending[0].length;
        this.board = new Tile[height][width]; // Inicializa el tablero vacío
        this.starting = new Tile[height][width]; // Inicializa el estado inicial
    }

    // Constructor que inicializa el estado inicial y final
    public Puzzle(Tile[][] starting, Tile[][] ending) {
        this.starting = starting;
        this.ending = ending;
        this.height = starting.length;
        this.width = starting[0].length;
        this.board = new Tile[height][width]; // Inicializa el tablero vacío
        initializeTiles(); // Método para llenar el tablero con las fichas iniciales
    }

    // Método para llenar el tablero con las fichas iniciales
    private void initializeTiles() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (starting[i][j] != null) {
                    board[i][j] = new Tile(i, j, starting[i][j].getColorChar());
                }
            }
        }
    }


    public int getHeight() {
        return height; // Devuelve la altura del tablero
    }

    public int getWidth() {
        return width; // Devuelve la anchura del tablero
    }

    // Método para obtener una baldosa en una posición
    public Tile getTile(int row, int column) {
        if (isValidPosition(row, column)) {
            return board[row][column]; // Devuelve la baldosa si es válida
        }
        return null; // Devuelve null si la posición no es válida
    }

    
    public boolean isValidPosition(int row, int column) {
        // Verifica que las filas y columnas estén dentro de los límites del tablero
        return row >= 0 && row < height && column >= 0 && column < width;
    }


    // Otros constructores omitidos por brevedad...

    public void addTile(int row, int column, char colorChar) {
        // Validar que no se coloque una ficha blanca en un hueco
        if (colorChar == 'w' && isHoleAt(row, column)) {
            System.out.println("No se puede colocar una ficha blanca en un hueco.");
            return; // Salir del método o lanzar una excepción
        }
    
        // Obtener el color correspondiente usando el método getColor
        String color = getColor(colorChar);
    
        // Lógica para añadir la ficha si la posición es válida
        if (isValidPosition(row, column)) {
            board[row][column] = new Tile(row, column, colorChar);
            System.out.println("Ficha añadida en (" + row + ", " + column + ") con color " + color);
        } else {
            System.out.println("Posición inválida para añadir la ficha.");
        }
    }

    // Método auxiliar para obtener el color de la ficha basado en un carácter
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
            case 'w':
                return "white"; // Para el hueco
            default:
                return "black"; // Color por defecto
        }
    }

    private boolean isHoleAt(int row, int column) {
        Tile tile = board[row][column];
        return tile != null && "hole".equals(tile.getType());
    }
    
    // Método para crear un hueco
    public void makeHole(int row, int column) {
        if (isValidPosition(row, column)) {
            // Verificar si ya hay un hueco en esa posición
            if (isHoleAt(row, column)) {
                System.out.println("Ya hay un hueco en esta posición.");
                return;
            }
    
            // Crear un hueco en la posición
            board[row][column] = new Tile(row, column, 'w'); // 'w' para el hueco
            System.out.println("Hueco creado en (" + row + ", " + column + ").");
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
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

    private void tiltUp() {
        for (int column = 0; column < width; column++) {
            int targetRow = 0; // Empezar desde la primera fila
            for (int row = 0; row < height; row++) { // Iterar de arriba hacia abajo
                Tile currentTile = board[row][column];
                if (currentTile != null) { // Si encontramos una ficha
                    // Mover la ficha a la posición vacía
                    if (board[targetRow][column] == null) { // Verificar si la posición destino está vacía
                        moveTile(new int[]{row, column}, new int[]{targetRow, column}); // Mover la ficha
                        targetRow++; // Actualizar la fila destino
                    } else {
                        // Si la posición destino no está vacía, verificar si es un hueco
                        if (isHoleAt(targetRow, column)) {
                            deleteTile(row, column); // Eliminar la ficha si es un hueco
                            System.out.println("La ficha cayó en un hueco y fue eliminada.");
                        } else if (currentTile.getColorChar() == board[targetRow][column].getColorChar()) {
                            // Si las fichas pueden fusionarse
                            deleteTile(row, column); // Eliminar la ficha original
                            board[targetRow][column] = new Tile(targetRow, column, currentTile.getColorChar()); // Pegar la ficha
                            System.out.println("Baldosa pegada en (" + targetRow + ", " + column + ").");
                        }
                        // Ajustar para la siguiente ficha
                        targetRow++; // Pasar a la siguiente fila objetivo
                    }
                }
            }
        }
    }
    
    private void tiltDown() {
        for (int column = 0; column < width; column++) {
            int targetRow = height - 1; // Empezar desde la última fila
            for (int row = height - 1; row >= 0; row--) { // Iterar de abajo hacia arriba
                Tile currentTile = board[row][column];
                if (currentTile != null) { // Si encontramos una ficha
                    // Mover la ficha a la posición vacía
                    if (board[targetRow][column] == null) { // Verificar si la posición destino está vacía
                        moveTile(new int[]{row, column}, new int[]{targetRow, column}); // Mover la ficha
                        targetRow--; // Actualizar la fila destino
                    } else {
                        // Si la posición destino no está vacía, verificar si es un hueco
                        if (isHoleAt(targetRow, column)) {
                            deleteTile(row, column); // Eliminar la ficha si es un hueco
                            System.out.println("La ficha cayó en un hueco y fue eliminada.");
                        } else if (currentTile.getColorChar() == board[targetRow][column].getColorChar()) {
                            // Si las fichas pueden fusionarse
                            deleteTile(row, column); // Eliminar la ficha original
                            board[targetRow][column] = new Tile(targetRow, column, currentTile.getColorChar()); // Pegar la ficha
                            System.out.println("Baldosa pegada en (" + targetRow + ", " + column + ").");
                        }
                        // Ajustar para la siguiente ficha
                        targetRow--; // Pasar a la siguiente fila objetivo
                    }
                }
            }
        }
    }
    
    private void tiltLeft() {
        for (int row = 0; row < height; row++) {
            int targetColumn = 0; // Empezar desde la primera columna
            for (int column = 0; column < width; column++) { // Iterar de izquierda a derecha
                Tile currentTile = board[row][column];
                if (currentTile != null) { // Si encontramos una ficha
                    // Mover la ficha a la posición vacía
                    if (board[row][targetColumn] == null) { // Verificar si la posición destino está vacía
                        moveTile(new int[]{row, column}, new int[]{row, targetColumn}); // Mover la ficha
                        targetColumn++; // Actualizar la columna destino
                    } else {
                        // Si la posición destino no está vacía, verificar si es un hueco
                        if (isHoleAt(row, targetColumn)) {
                            deleteTile(row, column); // Eliminar la ficha si es un hueco
                            System.out.println("La ficha cayó en un hueco y fue eliminada.");
                        } else if (currentTile.getColorChar() == board[row][targetColumn].getColorChar()) {
                            // Si las fichas pueden fusionarse
                            deleteTile(row, column); // Eliminar la ficha original
                            board[row][targetColumn] = new Tile(row, targetColumn, currentTile.getColorChar()); // Pegar la ficha
                            System.out.println("Baldosa pegada en (" + row + ", " + targetColumn + ").");
                        }
                        // Ajustar para la siguiente ficha
                        targetColumn++; // Pasar a la siguiente columna objetivo
                    }
                }
            }
        }
    }
    
    private void tiltRight() {
        for (int row = 0; row < height; row++) {
            int targetColumn = width - 1; // Empezar desde la última columna
            for (int column = width - 1; column >= 0; column--) { // Iterar de derecha a izquierda
                Tile currentTile = board[row][column];
                if (currentTile != null) { // Si encontramos una ficha
                    // Mover la ficha a la posición vacía
                    if (board[row][targetColumn] == null) { // Verificar si la posición destino está vacía
                        moveTile(new int[]{row, column}, new int[]{row, targetColumn}); // Mover la ficha
                        targetColumn--; // Actualizar la columna destino
                    } else {
                        // Si la posición destino no está vacía, verificar si es un hueco
                        if (isHoleAt(row, targetColumn)) {
                            deleteTile(row, column); // Eliminar la ficha si es un hueco
                            System.out.println("La ficha cayó en un hueco y fue eliminada.");
                        } else if (currentTile.getColorChar() == board[row][targetColumn].getColorChar()) {
                            // Si las fichas pueden fusionarse
                            deleteTile(row, column); // Eliminar la ficha original
                            board[row][targetColumn] = new Tile(row, targetColumn, currentTile.getColorChar()); // Pegar la ficha
                            System.out.println("Baldosa pegada en (" + row + ", " + targetColumn + ").");
                        }
                        // Ajustar para la siguiente ficha
                        targetColumn--; // Pasar a la siguiente columna objetivo
                    }
                }
            }
        }
    }

        public boolean isGoal() {
        // Comprobar si el estado actual es igual al estado final
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                Tile currentTile = board[row][column];
                Tile endingTile = ending[row][column];

                // Comprobar si hay una ficha en la posición actual
                if (currentTile == null && endingTile != null) {
                    return false; // Si hay una ficha en la meta y no en la posición actual
                }
                if (currentTile != null && endingTile == null) {
                    return false; // Si hay una ficha en la posición actual y no en la meta
                }
                if (currentTile != null && endingTile != null) {
                    if (currentTile.getColorChar() != endingTile.getColorChar()) {
                        return false; // Si los colores no coinciden
                    }
                }
            }
        }
        return true; // Todos los board coinciden
    }

    public void deleteTile(int row, int column) {
        board[row][column] = null; // Eliminar la ficha
    }

    public void moveTile(int[] from, int[] to) {
        board[to[0]][to[1]] = board[from[0]][from[1]]; // Mover la ficha
        board[from[0]][from[1]] = null; // Eliminar la ficha de la posición original
    }

    // Método para imprimir el tablero, útil para depuración
    public void printBoard() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                System.out.print(tile != null ? tile.getColorChar() : "x"); // 'x' para indicar una posición vacía
            }
            System.out.println();
        }
    }
}