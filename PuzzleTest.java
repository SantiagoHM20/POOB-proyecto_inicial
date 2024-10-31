import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class PuzzleTest {
    
    private Puzzle puzzle;

    @BeforeEach
    public void setUp() {
        puzzle = new Puzzle(3, 3); 
    }

    @Test
    public void shouldCreatePuzzleWithGivenHeightAndWidth() {
        int height = 3;
        int width = 3;

        Puzzle newPuzzle = new Puzzle(height, width);

        assertNotNull(newPuzzle); // Verifica que el rompecabezas se ha creado
        assertEquals(height, newPuzzle.getHeight()); // Verifica la altura
        assertEquals(width, newPuzzle.getWeight()); // Verifica el ancho
        assertNotNull(newPuzzle.getStarting()); // Verifica que la matriz inicial no es nula
        assertNotNull(newPuzzle.getEnding()); // Verifica que la matriz final no es nula
    }

    @Test
    public void shouldNotCreatePuzzleWithNegativeDimensions() {
        int height = -1;
        int width = -1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Puzzle(height, width);
        });

        assertEquals("Las dimensiones deben ser positivas", exception.getMessage());
    }

    @Test
    public void shouldCreatePuzzleWithFinalBoardOnly() {
        char[][] finalBoard = {{'.', '.', '.'}, {'.', 'r', '.'}, {'.', '.', '.'}};

        Puzzle newPuzzle = new Puzzle(finalBoard);

        assertNotNull(newPuzzle); 
        assertNotNull(newPuzzle.getEnding());
        assertNotNull(newPuzzle.getStarting()); 
    }

    @Test
    public void shouldNotCreatePuzzleWithNullFinalBoard() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Puzzle((char[][]) null);
        });

        assertEquals("El tablero final no puede ser nulo", exception.getMessage());
    }

    @Test
    public void shouldAddTileWhenPositionIsFree() {
        int row = 1;
        int column = 1;
        char tileChar = 'r'; 

        puzzle.addTile(row, column, tileChar);

        assertNotNull(puzzle.getStarting()[row][column]); 
        assertEquals(tileChar, puzzle.getStarting()[row][column].getType()); 
    }

    @Test
    public void shouldNotAddTileWhenPositionIsOccupied() {
        int row = 1;
        int column = 1;
        char tileChar = 'r'; 

       
        puzzle.addTile(row, column, tileChar);
        
       
        puzzle.addTile(row, column, 'g');

        assertEquals(tileChar, puzzle.getStarting()[row][column].getType()); 
        assertEquals(50, puzzle.getStarting()[row][column].getXPosition()); 
        assertEquals(50, puzzle.getStarting()[row][column].getYPosition()); 
    }

    @Test
    public void shouldNotAddTileWhenPositionIsOutOfBounds() {
        int row = -1; 
        int column = 1;
        char tileChar = 'r';

        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        puzzle.addTile(row, column, tileChar);
        
        assertTrue(outContent.toString().contains("Coordenadas fuera de los límites del tablero.")); 
    }

    @Test
    public void shouldNotAddTileWhenColumnIsOutOfBounds() {
        int row = 1;
        int column = 4; 
        char tileChar = 'r';

     
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        puzzle.addTile(row, column, tileChar);
        
        assertTrue(outContent.toString().contains("Coordenadas fuera de los límites del tablero.")); // Verifica mensaje de error
    }
     @Test
    public void shouldDeleteTileWhenPositionIsOccupied() {
        int row = 1;
        int column = 1;
        char tileChar = 'r';

      
        puzzle.addTile(row, column, tileChar);
        
        puzzle.deleteTile(row, column);

        assertNull(puzzle.getStarting()[row][column]); 
    }

    @Test
    public void shouldNotDeleteTileWhenPositionIsEmpty() {
        int row = 1;
        int column = 1;

     
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        puzzle.deleteTile(row, column);

        assertTrue(outContent.toString().contains("No hay ficha en la posición indicada.")); // Verifica mensaje de error
    }

    @Test
    public void shouldNotDeleteTileWhenPositionIsOutOfBounds() {
        int row = -1; 
        int column = 1;

      
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        puzzle.deleteTile(row, column);

        assertTrue(outContent.toString().contains("Coordenadas fuera de los límites del tablero.")); 
    }

    @Test
    public void shouldNotDeleteTileWhenColumnIsOutOfBounds() {
        int row = 1;
        int column = 4; 

      
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        puzzle.deleteTile(row, column);

        assertTrue(outContent.toString().contains("Coordenadas fuera de los límites del tablero.")); 
    }
    @Test
public void shouldAddGlueToTileAndIncludeAdjacentTiles() {
    // Agrega una ficha en (0,0) y otra adyacente en (0,1)
    Tile tile00 = puzzle.getStarting()[0][0];
    Tile tile01 = puzzle.getStarting()[0][1];

    // Act: aplica pegamento en la ficha en (0,0)
    puzzle.addGlue(0, 0); 

    // Assert: verifica que la ficha en (0,0) esté pegada
    assertTrue("Tile at (0,0) should be glued", Glue.isGlued(tile00));
    
    // Verifica que la ficha adyacente en (0,1) también esté pegada
    assertTrue("Adjacent tile at (0,1) should be glued", Glue.isGlued(tile01));

    // Verifica que se haya creado un solo grupo de pegamento y que incluya ambas fichas
    ArrayList<ArrayList<Tile>> glueGroups = puzzle.getGlue();
    assertEquals("Only one glue group should be created", 1, glueGroups.size());
    assertTrue("Glue group should contain the tile at (0,0)", glueGroups.get(0).contains(tile00));
    assertTrue("Glue group should contain the adjacent tile at (0,1)", glueGroups.get(0).contains(tile01));
}

    @Test
    public void shouldNotAddGlueIfTileIsAlreadyGlued() {
        puzzle.addGlue(0, 0); 

 
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

     
        puzzle.addGlue(0, 0);
        
        assertTrue(outContent.toString().contains("La ficha ya está pegada")); 
    }

    @Test
    public void shouldNotAddGlueWhenPositionIsOutOfBounds() {
        int row = -1; 
        int column = 0;

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        puzzle.addGlue(row, column);

        assertTrue(outContent.toString().contains("La posición no es parte del tablero")); 
    }
    @Test
    public void shouldCreateHoleAtValidPosition() {
        puzzle.makeHole(1, 1);
        assertNotNull(puzzle.getStarting()[1][1]);
        assertEquals("hole", puzzle.getStarting()[1][1].getTypet());
    }
    
    @Test
    public void shouldNotCreateHoleIfAlreadyExists() {
        puzzle.makeHole(1, 1);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    
        puzzle.makeHole(1, 1);
        assertTrue(outContent.toString().contains("Ya hay un hueco en esta posición."));
    }
    
    @Test
    public void shouldNotCreateHoleWhenPositionIsOutOfBounds() {
        int row = 3;
        int column = 3;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    
        puzzle.makeHole(row, column);
        assertTrue(outContent.toString().contains("Coordenadas fuera de los límites del tablero."));
    }
    @Test
    public void shouldRelocateTileToValidPosition() {
        puzzle.addTile(1, 1, 'r');
        int[] from = {1, 1};
        int[] to = {1, 2};
        puzzle.relocate(from, to);
        
        assertNull(puzzle.getStarting()[1][1]);
        assertNotNull(puzzle.getStarting()[1][2]);
    }
    
    @Test
    public void shouldNotRelocateIfSourceIsEmpty() {
        int[] from = {1, 1};
        int[] to = {1, 2};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        puzzle.relocate(from, to);
        assertTrue(outContent.toString().contains("No hay ficha en la posición de origen."));
    }
    
    @Test
    public void shouldNotRelocateToOccupiedPosition() {
        puzzle.addTile(1, 1, 'r');
        puzzle.addTile(1, 2, 'g');
        int[] from = {1, 1};
        int[] to = {1, 2};
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        puzzle.relocate(from, to);
        assertEquals('r', puzzle.getStarting()[1][1].getType());
        assertEquals('g', puzzle.getStarting()[1][2].getType());
    }
    
    @Test
    public void shouldNotRelocateToHole() {
        puzzle.addTile(1, 1, 'r');
        puzzle.makeHole(1, 2);
        int[] from = {1, 1};
        int[] to = {1, 2};
        
        puzzle.relocate(from, to);
        assertNotNull(puzzle.getStarting()[1][1]);
        assertEquals('r', puzzle.getStarting()[1][1].getType());
    }
    
    @Test
    public void shouldNotRelocateWhenPositionsAreOutOfBounds() {
        int[] from = {3, 3};
        int[] to = {1, 2};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        puzzle.relocate(from, to);
        assertTrue(outContent.toString().contains("Coordenadas fuera de los límites del tablero."));
    }
    @Test
    public void shouldExchangeTilesBetweenBoards() {
        puzzle.addTile(1, 1, 'r');
        puzzle.addTile(2, 2, 'g');
        puzzle.makeHole(1, 2);
        puzzle.makeHole(2, 1);
        
        puzzle.exchange();
        
        assertNull(puzzle.getStarting()[1][1]);
        assertNull(puzzle.getStarting()[2][2]);
        assertNotNull(puzzle.getEnding()[1][2]);
        assertNotNull(puzzle.getEnding()[2][1]);
    }
    
    @Test
    public void shouldNotChangePositionsIfBothBoardsAreEmpty() {
        puzzle.exchange();
        
        assertNull(puzzle.getStarting()[0][0]);
        assertNull(puzzle.getEnding()[0][0]);
    }
    
    @Test
    public void shouldMaintainVisibilityAfterExchange() {
        puzzle.addTile(1, 1, 'r');
        puzzle.addTile(2, 2, 'g');
        
        puzzle.exchange();
        
        assertTrue(puzzle.getEnding()[1][1].isVisible());
        assertTrue(puzzle.getStarting()[2][2].isVisible());
    }
    
    @Test
    public void shouldHandleExchangeWithHolesCorrectly() {
        puzzle.addTile(1, 1, 'r');
        puzzle.makeHole(2, 2);
        puzzle.exchange();
        
        assertNotNull(puzzle.getStarting()[2][2]);
        assertEquals("hole", puzzle.getStarting()[2][2].getTypet());
        assertNull(puzzle.getEnding()[1][1]);
    }
    
    @Test
    public void shouldKeepTilesInCorrectPositionsAfterExchange() {
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(1, 1, 'g');
        
        puzzle.exchange();
        
        assertEquals('r', puzzle.getEnding()[0][0].getType());
        assertEquals('g', puzzle.getEnding()[1][1].getType());
    }
    @Test
    public void shouldTiltLeftWhenItMinimizesMisplacedTiles() {
        puzzle.addTile(1, 1, 'r');
        puzzle.addTile(1, 2, 'g');
        puzzle.addTile(2, 1, 'b');
        puzzle.addTile(2, 2, 'y');
        puzzle.tilt();
    
       
        assertEquals('g', puzzle.getStarting()[1][1].getType());
        assertEquals('r', puzzle.getStarting()[1][0].getType());
    }
    
    @Test
    public void shouldTiltRightWhenItMinimizesMisplacedTiles() {
        puzzle.addTile(0, 1, 'r');
        puzzle.addTile(1, 1, 'g');
        puzzle.addTile(2, 1, 'b');
        puzzle.addTile(2, 0, 'y');
        puzzle.tilt();
    
        assertEquals('g', puzzle.getStarting()[1][1].getType());
        assertEquals('r', puzzle.getStarting()[1][2].getType());
    }
    
    @Test
    public void shouldTiltUpWhenItMinimizesMisplacedTiles() {
        puzzle.addTile(1, 1, 'g');
        puzzle.addTile(1, 2, 'r');
        puzzle.tilt();
    
      
        assertEquals('g', puzzle.getStarting()[0][1].getType());
        assertEquals('r', puzzle.getStarting()[0][2].getType());
    }
    
    @Test
    public void shouldTiltDownWhenItMinimizesMisplacedTiles() {
        puzzle.addTile(0, 1, 'g');
        puzzle.addTile(1, 1, 'r');
        puzzle.tilt();
    
        assertEquals('r', puzzle.getStarting()[2][1].getType());
        assertEquals('g', puzzle.getStarting()[1][1].getType());
    }
    
    @Test
    public void shouldNotTiltIfAllTilesAreInCorrectPosition() {
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(1, 0, 'g');
        puzzle.tilt();
    
        assertEquals('r', puzzle.getStarting()[0][0].getType());
        assertEquals('g', puzzle.getStarting()[1][0].getType());
    }
    @Test
    public void shouldTiltLeftAndMoveTiles() {
        puzzle.addTile(1, 2, 'a');
        puzzle.addTile(1, 1, 'b');
        puzzle.tilt('l');
    
        assertEquals('a', puzzle.getStarting()[1][1].getType());
        assertNull(puzzle.getStarting()[1][2]);
    }
    
    @Test
    public void shouldTiltRightAndMoveTiles() {
        puzzle.addTile(1, 0, 'a');
        puzzle.addTile(1, 1, 'b');
        puzzle.tilt('r');
    
        assertEquals('b', puzzle.getStarting()[1][2].getType());
        assertNull(puzzle.getStarting()[1][1]);
    }
    
    @Test
    public void shouldTiltUpAndMoveTiles() {
        puzzle.addTile(2, 0, 'a');
        puzzle.addTile(1, 0, 'b');
        puzzle.tilt('u');
    
        assertEquals('b', puzzle.getStarting()[0][0].getType());
        assertNull(puzzle.getStarting()[1][0]);
    }
    
    @Test
    public void shouldTiltDownAndMoveTiles() {
        puzzle.addTile(0, 0, 'a');
        puzzle.addTile(1, 0, 'b');
        puzzle.tilt('d');
    
        assertEquals('b', puzzle.getStarting()[2][0].getType());
        assertNull(puzzle.getStarting()[1][0]);
    }
    
    @Test
    public void shouldNotTiltIfNoTilesCanMove() {
        puzzle.addTile(1, 0, 'a');
        puzzle.addTile(1, 1, 'b');
        puzzle.tilt('l');
    
        assertEquals('a', puzzle.getStarting()[1][0].getType());
        assertEquals('b', puzzle.getStarting()[1][1].getType());
    }
    @Test
    public void shouldCountMisplacedTiles() {
        puzzle.addTile(0, 0, 'a');
        puzzle.addTile(1, 0, 'b');
        puzzle.addTile(2, 0, 'c');
    
        puzzle.getEnding()[0][0] = new Tile(50, 50, 0, 0, "red", 'a');
        puzzle.getEnding()[1][0] = new Tile(50, 50, 0, 50, "blue", 'b');
        puzzle.getEnding()[2][0] = new Tile(50, 50, 0, 100, "green", 'd'); // Mal
    
        int misPlacedCount = puzzle.misPlacedTiles();
    
        assertEquals(1, misPlacedCount);
    }
    
    @Test
    public void shouldReturnZeroIfAllTilesAreCorrect() {
        puzzle.addTile(0, 0, 'a');
        puzzle.addTile(1, 0, 'b');
        puzzle.addTile(2, 0, 'c');
    
        puzzle.getEnding()[0][0] = new Tile(50, 50, 0, 0, "red", 'a');
        puzzle.getEnding()[1][0] = new Tile(50, 50, 0, 50, "blue", 'b');
        puzzle.getEnding()[2][0] = new Tile(50, 50, 0, 100, "green", 'c'); // Bien
    
        int misPlacedCount = puzzle.misPlacedTiles();
    
        assertEquals(0, misPlacedCount);
    }
    
    @Test
    public void shouldReturnCorrectCountWithNullTiles() {
        puzzle.addTile(0, 0, 'a');
        puzzle.addTile(1, 0, 'b');
    
        puzzle.getEnding()[0][0] = new Tile(50, 50, 0, 0, "red", 'a');
        puzzle.getEnding()[1][0] = new Tile(50, 50, 0, 50, "blue", 'c'); // Mal 
        puzzle.getEnding()[2][0] = null; // Sin ficha
    
        int misPlacedCount = puzzle.misPlacedTiles();
    
        assertEquals(1, misPlacedCount);
    }
    public void shouldReturnTrueWhenBoardsMatch() {
        puzzle.addTile(0, 0, 'a');
        puzzle.addTile(0, 1, 'b');
        puzzle.addTile(1, 0, 'c');
    
        puzzle.getEnding()[0][0] = new Tile(50, 50, 0, 0, "red", 'a');
        puzzle.getEnding()[0][1] = new Tile(50, 50, 0, 50, "blue", 'b');
        puzzle.getEnding()[1][0] = new Tile(50, 50, 0, 100, "green", 'c');
    
        boolean result = puzzle.isGoal();
    
        assertTrue(result);
    }
    
    @Test
    public void shouldReturnFalseWhenTilesAreMissing() {
        puzzle.addTile(0, 0, 'a');
        
        puzzle.getEnding()[0][0] = new Tile(50, 50, 0, 0, "red", 'a');
        puzzle.getEnding()[0][1] = new Tile(50, 50, 0, 50, "blue", 'b'); // Falta
    
        boolean result = puzzle.isGoal();
    
        assertFalse(result);
    }
    
    @Test
    public void shouldReturnFalseWhenTilesDoNotMatch() {
        puzzle.addTile(0, 0, 'a');
    
        puzzle.getEnding()[0][0] = new Tile(50, 50, 0, 0, "red", 'a');
        puzzle.getEnding()[0][1] = new Tile(50, 50, 0, 50, "blue", 'c'); // Diferente
    
        boolean result = puzzle.isGoal();
    
        assertFalse(result);
    }
    
    @Test
    public void shouldReturnTrueWhenBothBoardsAreEmpty() {
        boolean result = puzzle.isGoal();
    
        assertTrue(result);
    }
    public void shouldTerminateProgram() {
        // Usar un interceptor para comprobar si System.exit fue llamado.
        SecurityManager securityManager = new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
                if (perm.getName().equals("exitVM.0")) {
                    throw new SecurityException("System.exit() called");
                }
            }
        };
        
        System.setSecurityManager(securityManager);
    
        try {
            puzzle.finish();
            fail("Se esperaba una excepción de seguridad debido a System.exit()");
        } catch (SecurityException e) {
            // Se ha capturado la excepción como se esperaba.
        } finally {
            System.setSecurityManager(null); // Restaurar el SecurityManager
        }
    }
    
    @Test
    public void shouldReturnCurrentArrangement() {
        Tile[][] expectedArrangement = new Tile[3][3];
        expectedArrangement[0][0] = new Tile(50, 50, 0, 0, "red", "ficha");
        expectedArrangement[1][1] = new Tile(50, 50, 50, 50, "blue", "ficha");
    
        Puzzle puzzle = new Puzzle(3, 3);
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(1, 1, 'b');
    
        Tile[][] actualArrangement = puzzle.actualArrangement();
    
        assertArrayEquals(expectedArrangement, actualArrangement);
    }
    
    @Test
    public void shouldReturnEmptyArrangement() {
        Puzzle puzzle = new Puzzle(3, 3);
        Tile[][] actualArrangement = puzzle.actualArrangement();
    
        Tile[][] expectedArrangement = new Tile[3][3]; // Arreglo vacío
        assertArrayEquals(expectedArrangement, actualArrangement);
    }

    public void shouldMakeAllTilesVisible() {
        Puzzle puzzle = new Puzzle(3, 3);
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(1, 1, 'b');
    
        puzzle.makeVisible();
    
        assertTrue(puzzle.board.isVisible());
        assertTrue(puzzle.actualArrangement()[0][0].isVisible());
        assertTrue(puzzle.actualArrangement()[1][1].isVisible());
    }
    
    @Test
    public void shouldMakeAllTilesInvisible() {
        Puzzle puzzle = new Puzzle(3, 3);
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(1, 1, 'b');
        puzzle.makeVisible();
    
        puzzle.makeInvisible();
    
        assertFalse(puzzle.board.isVisible());
        assertFalse(puzzle.actualArrangement()[0][0].isVisible());
        assertFalse(puzzle.actualArrangement()[1][1].isVisible());
    }
    
    @Test
    public void shouldReturnTrueWhenBoardsAreIdentical() {
        Puzzle puzzle = new Puzzle(3, 3); 
    
     
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(0, 1, 'g');
        puzzle.addTile(0, 2, 'b');
    
      
        assertTrue(puzzle.ok());
    }
    @Test
    public void shouldReturnFalseWhenBoardsAreDifferent() {
        Puzzle puzzle = new Puzzle(3, 3);    
      
        puzzle.addTile(0, 0, 'r');
        puzzle.addTile(0, 1, 'g');
        
       
        char[][] finalBoard = {{'r', 'g', 'b'}, {'.', '.', '.'}, {'.', '.', '.'}};
        puzzle.setEnding(finalBoard); 
    
        assertFalse(puzzle.ok());
    }
    @After
    public void tearDown() {
        puzzle = new Puzzle(3, 3);
    }
}
