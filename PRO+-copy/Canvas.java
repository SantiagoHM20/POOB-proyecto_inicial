import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel {
    private static final int TILE_SIZE = 50; // Tamaño de cada baldosa
    private Puzzle puzzle;

    public Canvas(Puzzle puzzle) {
        this.puzzle = puzzle;
        JFrame frame = new JFrame("Puzzle Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(puzzle.getWidth() * TILE_SIZE + 50, puzzle.getHeight() * TILE_SIZE + 50);
        frame.add(this);
        frame.setVisible(true);
    }

    // Método para dibujar el estado actual del puzzle
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < puzzle.getHeight(); i++) {
            for (int j = 0; j < puzzle.getWidth(); j++) {
                Tile tile = puzzle.getTile(i, j);
                if (tile != null) {
                    g.setColor(getColor(tile.getColor()));
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                g.setColor(Color.BLACK);
                g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // Método auxiliar para convertir un color en texto a un objeto Color
    private Color getColor(String color) {
        switch (color.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.ORANGE;
            case "purple":
                return Color.MAGENTA;
            default:
                return Color.GRAY; // Color por defecto si no coincide
        }
    }

    // Método para actualizar la pantalla
    public void update() {
        repaint(); // Llama a paintComponent para actualizar la visualización
    }
}
