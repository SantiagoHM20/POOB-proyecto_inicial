public class PuzzleSimulator {
    private Puzzle puzzle;
    private boolean isVisible;
    private Canvas canvas;

    // Constructor que crea el rompecabezas con un tamaño fijo
    public PuzzleSimulator(int height, int width) {
        this.isVisible = true; // Comienza visible
        puzzle = new Puzzle(height, width); // Crea el tablero con el tamaño ingresado
        canvas = new Canvas(puzzle); // Crea el Canvas para dibujar
    }

    // Constructor que inicializa solo el estado final
    public PuzzleSimulator(Tile[][] ending) {
        this.isVisible = true; // Comienza visible
        puzzle = new Puzzle(ending); // Crea el tablero solo con el estado final
        canvas = new Canvas(puzzle); // Crea el Canvas para dibujar
    }

    // Constructor que inicializa el estado inicial y final
    public PuzzleSimulator(Tile[][] starting, Tile[][] ending) {
        this.isVisible = true; // Comienza visible
        puzzle = new Puzzle(starting, ending); // Crea el tablero con el estado inicial y final
        canvas = new Canvas(puzzle); // Crea el Canvas para dibujar
    }


    // Método para adicionar una baldosa en la posición (fila, columna) con un color dado
    public void addTile(int row, int column, char colorChar) {
        if (isVisible) {
            if (row >= 0 && row < puzzle.getHeight() && column >= 0 && column < puzzle.getWidth()) {
                // Llamar a addTile usando un char en lugar de un String
                puzzle.addTile(row, column, colorChar); // Pasar colorChar directamente
                updateArrangement(); 
                System.out.println("Baldosa agregada en (" + row + ", " + column + ") de color " + colorChar);
            } else {
                System.out.println("Posición inválida.");
            }
        }
    }

    // Método para eliminar una baldosa de la posición (fila, columna)
    public void deleteTile(int row, int column) {
        if (isVisible) {
            if (row >= 0 && row < puzzle.getHeight() && column >= 0 && column < puzzle.getWidth()) {
                puzzle.deleteTile(row, column);
                updateArrangement(); // Actualiza la pantalla
                System.out.println("Baldosa eliminada de (" + row + ", " + column + ")");
            } else {
                System.out.println("Posición inválida.");
            }
        }
    }

    // Método para reubicar una baldosa de una posición a otra
    public void moveTile(int[] from, int[] to) {
        if (isVisible) {
            if (puzzle.isValidPosition(from[0], from[1]) && puzzle.isValidPosition(to[0], to[1])) {
                Tile tile = puzzle.getTile(from[0], from[1]);
                if (tile != null) {
                    char colorChar = tile.getColorChar(); // Obtener el char del color
                    if (tile instanceof GlueTile) {
                        // Lógica para mover las GlueTiles (sin cambios)
                    } else {
                        // Mover solo la baldosa si no es una GlueTile
                        puzzle.deleteTile(from[0], from[1]); // Eliminar la baldosa de la posición original
                        puzzle.addTile(to[0], to[1], colorChar); // Añadir la baldosa en la nueva posición usando char directamente
                        updateArrangement(); // Actualiza la pantalla
                        System.out.println("Baldosa movida de (" + from[0] + ", " + from[1] + ") a (" + to[0] + ", " + to[1] + ")");
                    }
                } else {
                    System.out.println("No hay baldosa en la posición (" + from[0] + ", " + from[1] + ")");
                }
            } else {
                System.out.println("Una o ambas posiciones son inválidas.");
            }
        }
    }

    public void tilt(char direction) {
        if (isVisible) {
            puzzle.tilt(direction); // Llamar al método tilt de Puzzle
            updateArrangement(); // Actualiza la pantalla después de inclinar
            System.out.println("Tablero inclinado hacia " + direction);
        }
    }

    // Método para pegar baldosas adyacentes
    public void glue(int row, int column) {
        if (isVisible) {
            Tile tile = puzzle.getTile(row, column);
            if (tile instanceof GlueTile) {
                GlueTile glueTile = (GlueTile) tile;
                for (Tile gluedTile : glueTile.getGluedTiles()) {
                    System.out.println("Baldosa pegada: " + gluedTile.getColor());
                }
                System.out.println("Baldosa en (" + row + ", " + column + ") está pegada.");
            } else {
                System.out.println("No hay una baldosa pegable en (" + row + ", " + column + ").");
            }
        }
    }
    
        public void createHole(int row, int column) {
        puzzle.makeHole(row, column); // Llama al método makeHole de Puzzle
    }

    // Método para actualizar la disposición del tablero
    private void updateArrangement() {
        canvas.update(); // Actualiza el Canvas para mostrar el estado actual del juego
    }

    // Método para verificar si se ha alcanzado el objetivo
    public void checkGoal() {
        if (puzzle.isGoal()) {
            System.out.println("¡Has alcanzado el objetivo!");
        } else {
            System.out.println("Aún no has alcanzado el objetivo.");
        }
    }
}