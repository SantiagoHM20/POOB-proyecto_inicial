import java.util.Scanner;
import java.util.ArrayList;

/**
 * Represents a puzzle game where tiles can be moved, glued, and tilted.
 * 
 * @author Santiago Arteaga y Santiago Hurtado
 * @version 8/09/2024
 */
public class Puzzle {
    private int w;
    private int h;
    private char[][] Starting;
    private char[][] Ending;
    private boolean[][] glue;
    private String color;
    private ArrayList<Integer> from = new ArrayList<>();
    private ArrayList<Integer> to = new ArrayList<>();
    private ArrayList<Rectangle> rectangles = new ArrayList<>();

    /**
     * Constructor for the Puzzle class. Initializes the puzzle board with
     * the starting and ending configurations read from the input.
     */
    public Puzzle() {
        Scanner scanner = new Scanner(System.in);

        h = scanner.nextInt();
        w = scanner.nextInt();
        scanner.nextLine();

        Starting = new char[h][w];
        glue = new boolean[h][w];

        // Initialize the starting board
        for (int row = 0; row < h; row++) {
            Starting[row] = scanner.nextLine().toCharArray();
        }

        scanner.nextLine();

        // Initialize the ending board
        Ending = new char[h][w];

        for (int row = 0; row < h; row++) {
            Ending[row] = scanner.nextLine().toCharArray();
        }

        // Initialize rectangles for each tile in the board
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                if (Starting[row][column] != '.') {
                    Rectangle rectangle = new Rectangle(1, 1, column, row, color);
                    rectangles.add(rectangle);
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
                return "black"; // Default color for unknown tiles
        }
    }


    /**
     * Adds a tile of a specified color to a given position on the board.
     * 
     * @param row the row index where the tile should be added.
     * @param column the column index where the tile should be added.
     * @param color the color of the tile to be added.
     */
    public void addTile(int row, int column, char color) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            String colorName;
            switch (color) {
                case 'r':
                    colorName = "red";
                    break;
                case 'b':
                    colorName = "blue";
                    break;
                case 'y':
                    colorName = "yellow";
                    break;
                case 'g':
                    colorName = "green";
                    break;
                default:
                    System.out.println("Color no válido.");
                    return;
            }

            // Find the corresponding rectangle and update its color
            for (Rectangle rect : rectangles) {
                if (rect.getXPosition() == column * 50 && rect.getYPosition() == row * 50) {
                    rect.changeColor(colorName);
                    return;
                }
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    /**
     * Deletes the tile at the specified position on the board.
     * 
     * @param row the row index of the tile to be deleted.
     * @param column the column index of the tile to be deleted.
     */
   public void deleteTile(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            Starting[row][column] = '.';
            // Remove the rectangle from the list and make it invisible
            for (int i = 0; i < rectangles.size(); i++) {
                Rectangle rect = rectangles.get(i);
                if (rect.getXPosition() == column * 50 && rect.getYPosition() == row * 50) {
                    rect.makeInvisible();
                    rectangles.remove(i);
                    break;
                }
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    /**
     * Relocates a tile from one position to another if the destination is empty.
     * 
     * @param fromRow the row index of the tile to be moved.
     * @param fromColumn the column index of the tile to be moved.
     * @param toRow the row index of the destination position.
     * @param toColumn the column index of the destination position.
     */
    public void relocate(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (fromRow >= 0 && fromRow < h && fromColumn >= 0 && fromColumn < w &&
            toRow >= 0 && toRow < h && toColumn >= 0 && toColumn < w) {
            
            if (Starting[toRow][toColumn] == '.') {
                Starting[toRow][toColumn] = Starting[fromRow][fromColumn];
                Starting[fromRow][fromColumn] = '.';
                
                glue[toRow][toColumn] = glue[fromRow][fromColumn];
                glue[fromRow][fromColumn] = false;
                
            } else {
                System.out.println("La posición de destino no está vacía.");
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    /**
     * Adds glue to the tile at the specified position.
     * 
     * @param row the row index where the glue should be added.
     * @param column the column index where the glue should be added.
     */
    public void addGlue(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w && Starting[row][column] != '.') {
            glue[row][column] = true;
        } else {
            System.out.println("Fuera del límite");
        }
    }

    /**
     * Removes glue from the tile at the specified position.
     * 
     * @param row the row index where the glue should be removed.
     * @param column the column index where the glue should be removed.
     */
    public void deleteGlue(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w && glue[row][column]) {
            glue[row][column] = false;
        } else {
            System.out.println("Fuera del límite O no hay pegamento en esta ficha");
        }
    }

    /**
     * Tilts the board in the specified direction, causing all tiles to shift.
     * 
     * @param direction the direction in which to tilt the board ('L', 'R', 'U', 'D').
     */
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

    /**
     * Tilts the board to the left, moving all tiles to the leftmost empty positions.
     */
    private void tiltLeft() {
        for (int row = 0; row < h; row++) {
            int targetColumn = 0;
            for (int column = 0; column < w; column++) {
                if (Starting[row][column] != '.') {
                    Starting[row][targetColumn++] = Starting[row][column];
                }
            }
            while (targetColumn < w) {
                Starting[row][targetColumn++] = '.';
            }
        }
    }

    /**
     * Tilts the board to the right, moving all tiles to the rightmost empty positions.
     */
    private void tiltRight() {
        for (int row = 0; row < h; row++) {
            int targetColumn = w - 1;
            for (int column = w - 1; column >= 0; column--) {
                if (Starting[row][column] != '.') {
                    Starting[row][targetColumn--] = Starting[row][column];
                }
            }
            while (targetColumn >= 0) {
                Starting[row][targetColumn--] = '.';
            }
        }
    }

    /**
     * Tilts the board upward, moving all tiles to the topmost empty positions.
     */
    private void tiltUp() {
        for (int column = 0; column < w; column++) {
            int targetRow = 0;
            for (int row = 0; row < h; row++) {
                if (Starting[row][column] != '.') {
                    Starting[targetRow++][column] = Starting[row][column];
                }
            }
            while (targetRow < h) {
                Starting[targetRow++][column] = '.';
            }
        }
    }

    /**
     * Tilts the board downward, moving all tiles to the bottommost empty positions.
     */
    private void tiltDown() {
        for (int column = 0; column < w; column++) {
            int targetRow = h - 1;
            for (int row = h - 1; row >= 0; row--) {
                if (Starting[row][column] != '.') {
                    Starting[targetRow--][column] = Starting[row][column];
                }
            }
            while (targetRow >= 0) {
                Starting[targetRow--][column] = '.';
            }
        }
    }

    /**
     * Checks if the current arrangement of the board matches the ending configuration.
     * 
     * @return true if the current board matches the ending configuration, false otherwise.
     */
    public boolean isGoal() {
        char[][] currentArrangement = actualArrangement();
        
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                if (currentArrangement[row][column] != Ending[row][column]) {
                    System.out.println("no");
                    return false;
                }
            }
        }
        System.out.println("yes");
        return true;
    }

    /**
     * Returns the current arrangement of the board.
     * 
     */
    public char[][] actualArrangement() {
        return Starting;
    }

    /**
     * Makes the rectangle representing the puzzle visible.
     */
    public void makeVisible() {
        for (Rectangle rect : rectangles) {
            rect.makeVisible();
        }
    }
    
    /**
     * Makes all rectangles invisible.
     */
    public void makeInvisible() {
        for (Rectangle rect : rectangles) {
            rect.makeInvisible();
        }
    }
}

     
    

   
