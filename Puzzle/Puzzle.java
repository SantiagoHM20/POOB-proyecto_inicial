import java.util.Scanner;
import java.util.ArrayList;

public class Puzzle {
    private int w;
    private int h;
    private char[][] Starting;
    private char[][] Ending;
    private boolean[][] glue;
    private ArrayList<Tile> tiles = new ArrayList<>();

    // Constructor 1: Recibe alto y ancho
    public Puzzle(int h, int w) {
        this.h = h;
        this.w = w;
        this.Starting = new char[h][w];
        this.Ending = new char[h][w];
        this.glue = new boolean[h][w];
    }

    // Constructor 2: Recibe el estado final (ending)
    public Puzzle(char[][] ending) {
        this.Ending = ending;
        this.h = ending.length;
        this.w = ending[0].length;
        this.Starting = new char[h][w];
        this.glue = new boolean[h][w];
    }

    // Constructor 3: Recibe los estados inicial y final
    public Puzzle(char[][] starting, char[][] ending) {
        this.Starting = starting;
        this.Ending = ending;
        this.h = starting.length;
        this.w = starting[0].length;
        this.glue = new boolean[h][w];
        initializeTiles();
    }
     // Método para inicializar las fichas (tiles)
    private void initializeTiles() {
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
            case 'r': return "red";
            case 'b': return "blue";
            case 'y': return "yellow";
            case 'g': return "green";
            default: return "black"; // Color por defecto
        }
    }

    public void addTile(int row, int column, char color) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            String colorName = getColor(color);

            for (Tile tile : tiles) {
                if (tile.getXPosition() == column * 50 && tile.getYPosition() == row * 50) {
                    tile.changeColor(colorName);
                    tile.setType(color);
                    return;
                }
            }
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

    public void relocate(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (fromRow >= 0 && fromRow < h && fromColumn >= 0 && fromColumn < w &&
            toRow >= 0 && toRow < h && toColumn >= 0 && toColumn < w) {

            if (Starting[toRow][toColumn] == '.') {
                Starting[toRow][toColumn] = Starting[fromRow][fromColumn];
                Starting[fromRow][fromColumn] = '.';

                glue[toRow][toColumn] = glue[fromRow][fromColumn];
                glue[fromRow][fromColumn] = false;

                // Actualiza la posición del tile
                for (Tile tile : tiles) {
                    if (tile.getXPosition() == fromColumn * 50 && tile.getYPosition() == fromRow * 50) {
                        tile.makeInvisible();
                        tile = new Tile(50, 50, toColumn * 50, toRow * 50, getColor(Starting[toRow][toColumn]), Starting[toRow][toColumn]);
                        tiles.add(tile);
                        break;
                    }
                }
            } else {
                System.out.println("La posición de destino no está vacía.");
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }

    public void addGlue(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w && Starting[row][column] != '.') {
            glue[row][column] = true;
        } else {
            System.out.println("Fuera del límite");
        }
    }

    public void deleteGlue(int row, int column) {
        if (row >= 0 && row < h && column >= 0 && column < w && glue[row][column]) {
            glue[row][column] = false;
        } else {
            System.out.println("Fuera del límite o no hay pegamento en esta ficha");
        }
    }

    public void tilt(char direction) {
        switch (direction) {
            case 'L': tiltLeft(); break;
            case 'R': tiltRight(); break;
            case 'U': tiltUp(); break;
            case 'D': tiltDown(); break;
            default: System.out.println("Dirección inválida");
        }
    }

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

    public char[][] actualArrangement() {
        return Starting;
    }

    public void makeVisible() {
        for (Tile tile : tiles) {
            tile.makeVisible();
        }
    }

    public void makeInvisible() {
        for (Tile tile : tiles) {
            tile.makeInvisible();
        }
    }
}

     
    

   
