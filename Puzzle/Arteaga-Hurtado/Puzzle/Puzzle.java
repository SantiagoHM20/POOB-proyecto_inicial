import java.util.Scanner;
import java.util.ArrayList;

public class Puzzle {
    private int w;
    private int h;
    private char[][] Starting;
    private char[][] Ending;
    private boolean[][] glue;
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
            case 'r': return "red";
            case 'b': return "blue";
            case 'y': return "yellow";
            case 'g': return "green";
            default: return "black";
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

    public void relocate(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (fromRow >= 0 && fromRow < h && fromColumn >= 0 && fromColumn < w &&
            toRow >= 0 && toRow < h && toColumn >= 0 && toColumn < w) {
            if (Starting[toRow][toColumn] != '.') {
                System.out.println("Posición ya ocupada.");
                return; 
            }
            char tile = Starting[fromRow][fromColumn];
            Starting[toRow][toColumn] = tile;
            Starting[fromRow][fromColumn] = '.';
            glue[toRow][toColumn] = glue[fromRow][fromColumn];
            glue[fromRow][fromColumn] = false;

            for (int i = 0; i < tiles.size(); i++) {
                Tile tileObject = tiles.get(i);
                if (tileObject.getXPosition() == fromColumn * 50 && tileObject.getYPosition() == fromRow * 50) {
                    tileObject.makeInvisible();
                    tiles.remove(i);
                    break;
                }
            }

            Tile newTile = new Tile(50, 50, toColumn * 50, toRow * 50, getColor(tile), tile);
            tiles.add(newTile);
            newTile.makeVisible(); 
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
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
                    if (targetColumn < w && Starting[row][targetColumn] == '.') {
                        relocate(row, column, row, targetColumn);
                        targetColumn++;
                    } else if (column > 0) {
                        System.out.println("No se puede mover la ficha en (" + row + "," + column + ") hacia la izquierda.");
                    }
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
                    if (targetColumn >= 0 && Starting[row][targetColumn] == '.') {
                        relocate(row, column, row, targetColumn);
                        targetColumn--;
                    } else if (column < w - 1) {
                        System.out.println("No se puede mover la ficha en (" + row + "," + column + ") hacia la derecha.");
                    }
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
                    if (targetRow < h && Starting[targetRow][column] == '.') {
                        relocate(row, column, targetRow, column);
                        targetRow++;
                    } else if (row > 0) {
                        System.out.println("No se puede mover la ficha en (" + row + "," + column + ") hacia arriba.");
                    }
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
                    if (targetRow >= 0 && Starting[targetRow][column] == '.') {
                        relocate(row, column, targetRow, column);
                        targetRow--;
                    } else if (row < h - 1) {
                        System.out.println("No se puede mover la ficha en (" + row + "," + column + ") hacia abajo.");
                    }
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

     
    

   
