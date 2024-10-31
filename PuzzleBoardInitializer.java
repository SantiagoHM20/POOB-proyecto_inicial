public class PuzzleBoardInitializer {
    
    private static final int TILE_SIZE = 50;

    /**
     * Inicializa un tablero en el arreglo `starting` y muestra el rectángulo en la interfaz.
     */
    public static void initializeStartingBoard(Tile[][] starting, int h, int w) {
        Rectangle board = new Rectangle(h * TILE_SIZE, w * TILE_SIZE, 0, 0, "black");
        board.makeVisible();
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                starting[row][column] = null; 
            }
        }
    }

    /**
     * Inicializa el tablero `ending` con el rectángulo visible en una posición desplazada.
     */
    public static void initializeEndingBoard(Tile[][] ending, int h, int w) {
        int spacing = TILE_SIZE;  
        int finalOffsetX = w * TILE_SIZE + spacing; 
        Rectangle finalBoard = new Rectangle(h * TILE_SIZE, w * TILE_SIZE, finalOffsetX, 0, "black");
        finalBoard.makeVisible();
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                ending[row][column] = null;
            }
        }
    }

    /**
     * Convierte un tablero de caracteres a objetos Tile para el tablero inicial.
     */
    public static void convertCharsToStartingTiles(char[][] tableroInicial, Tile[][] starting, int h, int w) {
        Rectangle board = new Rectangle(h * TILE_SIZE, w * TILE_SIZE, 0, 0, "black");
        board.makeVisible();
        int spacing = TILE_SIZE;  
        int finalOffsetX = w * TILE_SIZE + spacing; 
        Rectangle finalBoard = new Rectangle(h * TILE_SIZE, w * TILE_SIZE, finalOffsetX, 0, "black");
        finalBoard.makeVisible();
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                char currentChar = tableroInicial[row][column];
                if (currentChar == '.') {
                    starting[row][column] = null;
                } else {
                    String color = getColorForTile(currentChar);
                    starting[row][column] = new Tile(TILE_SIZE, TILE_SIZE, column * TILE_SIZE, row * TILE_SIZE, color, currentChar);
                }
            }
        }
    }

    /**
     * Convierte un tablero de caracteres a objetos Tile para el tablero final.
     */
    public static void convertCharsToEndingTiles(char[][] tableroFinal, Tile[][] ending, int h, int w) {
        Rectangle board = new Rectangle(h * TILE_SIZE, w * TILE_SIZE, 0, 0, "black");
        board.makeVisible();
        int spacing = TILE_SIZE;  
        int finalOffsetX = w * TILE_SIZE + spacing; 
        Rectangle finalBoard = new Rectangle(h * TILE_SIZE, w * TILE_SIZE, finalOffsetX, 0, "black");
        finalBoard.makeVisible();
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                char currentChar = tableroFinal[row][column];
                if (currentChar == '.') {
                    ending[row][column] = null;
                } else {
                    String color = getColorForTile(currentChar);
                    ending[row][column] = new Tile(TILE_SIZE, TILE_SIZE, column * TILE_SIZE + finalOffsetX, row * TILE_SIZE, color, currentChar);
                }
            }
        }
    }

    /**
     * Devuelve el color correspondiente a un carácter de pieza del rompecabezas.
     */
    public static String getColorForTile(char tileChar) {
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
}
