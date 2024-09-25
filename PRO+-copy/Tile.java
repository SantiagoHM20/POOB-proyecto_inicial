public class Tile {
    private int row;
    private int column;
    private String color; // Cambiar a char si deseas manejarlo así
    private String type; // "ficha" o "hole"

    // Constructor para una ficha normal
    public Tile(int row, int column, char colorChar) {
        this.row = row;
        this.column = column;
        this.color = getColor(colorChar); // Convierte el char a String
        this.type = "ficha"; // Por defecto es una ficha
    }

    // Constructor para un hueco
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        this.color = "white"; // Los huecos son blancos
        this.type = "hole"; // Es un hueco
    }

    // Método para obtener el color correspondiente a un char
    private String getColor(char colorChar) {
        switch (colorChar) {
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

    public char getColorChar() {
        switch (color.toLowerCase()) {
            case "red":
                return 'r';
            case "blue":
                return 'b';
            case "yellow":
                return 'y';
            case "green":
                return 'g';
            default:
                return 'k'; // Por defecto, usa 'k' para negro
        }
    }

    // Método para actualizar la posición de la baldosa
    public void setPosition(int newRow, int newColumn) {
        this.row = newRow;
        this.column = newColumn;
    }

    public String getColor() {
        return this.color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getType() {
        return type; // Para acceder al tipo
    }
}
