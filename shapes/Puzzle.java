import java.util.Scanner;
import java.util.ArrayList;
/**
 * Write a description of class Puzzle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Puzzle {
    private int w;
    private int h;
    private int row;
    private int column;
    private char direction;
    private char[][] Starting;
    private char[][] Ending;
    private boolean isVisible;
    private String color;
    private ArrayList<Integer> from = new ArrayList<>();
    private ArrayList<Integer> to = new ArrayList<>();
    public Rectangle rectangle = new Rectangle();
    
    
    /**
     * Constructor para objetos de la clase Puzzle
     */
    public Puzzle() {
        Scanner scanner = new Scanner(System.in);
        
        h = scanner.nextInt();
        w = scanner.nextInt();
        scanner.nextLine(); 

        Starting = new char[h][w];
        
        // Instanciar el tablero inicial
        for (int row = 0; row < h; row++) {
            Starting[row] = scanner.nextLine().toCharArray();
        }
        
        scanner.nextLine();
        
        // Instanciar el tablero final
        Ending = new char[h][w];
        
        for (int row = 0; row < h; row++) {
            Ending[row] = scanner.nextLine().toCharArray();
        }
    }
    
    public void addTile(int row, int column, char color) {
        // Verificar si las coordenadas están dentro del tablero
        if (row >= 0 && row < h && column >= 0 && column < w) {
            // Asignar color según el caracter
            switch (color) {
                case 'r':
                    rectangle.changeColor("red");
                    break;
                case 'b':
                    rectangle.changeColor("blue");
                    break;
                case 'y':
                    rectangle.changeColor("yellow");
                    break;
                case 'g':
                    rectangle.changeColor("green");
                    break;
                default:
                    System.out.println("Color no válido.");
                    return;
            }
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }
    
    public void deleteTile(int row, int column) {
        // Verificar si las coordenadas están dentro del tablero
        if (row >= 0 && row < h && column >= 0 && column < w) {
            Starting[row][column] = '.';
        } else {
            System.out.println("Coordenadas fuera de los límites del tablero.");
        }
    }
    
    public void relocate() {
      
    }
    
    public void addGlue(int row, int column) {
       
    }
    
    public void deleteGlue(int row, int column) {
       
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
        
        if(Starting[h][w] == Ending[h][w]){
            System.out.println("yes");
            return true;
          }
        else{
        System.out.println("no");    
        return false;
        }
    }

    public Puzzle actualArrangement() {
     
        return this;
    }

    public void makeVisible() {
        rectangle.makeVisible();
    }
    
    public void makeInvisible() {
        rectangle.makeInvisible();
    }
    
    public static void main(String[] args) {
       
        Puzzle puzzle = new Puzzle();
       
        puzzle.tilt('L'); 
    }
}

     
    

   
