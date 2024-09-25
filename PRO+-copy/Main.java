public class Main {
    public static void main(String[] args) {
        PuzzleSimulator simulator = new PuzzleSimulator(5, 5); // Crear un tablero de 5x5
        
        simulator.addTile(2, 2, "red");       // Añadir una baldosa roja en (2, 2)
        simulator.addTile(1, 3, "blue");      // Añadir una baldosa azul en (1, 3)
        simulator.addTile(0, 0, "green");     // Añadir una baldosa verde en (0, 0)
        
        simulator.moveTile(2, 2, 3, 3);       // Mover la baldosa de (2, 2) a (3, 3)
        
        simulator.deleteTile(1, 3);           // Eliminar la baldosa azul en (1, 3)
    }
}
