
/**
 * Esta clase resuelve por si sola Puzzle, pasa de el tablero inicial al tablero final
 * 

 * @author Arteaga-Hurtado
 * @version 24/10/2024
 *///
public class PuzzleContest
{
    public boolean solve(char[][] starting, char[][] ending){
    Puzzle puzzle = new Puzzle(starting, ending);
   
        
     return false;

    }

    public void simulate(char[][] starting, char[][] ending){
    Puzzle puzzle = new Puzzle(starting, ending);
    Puzzle.tilt();
    
    }

      public static void main(String[] args) {
        char[][] starting = {
            {'r', 'g', 'b'},
            {'y', '.', 'y'},
            {'b', 'r', '.'}
        };

        char[][] ending = {
            {'b', 'r', 'g'},
            {'y', 'y', 'r'},
            {'.', 'b', '.'}
        };

        PuzzleContest contest = new PuzzleContest();
        contest.simulate(starting, ending);
        if (contest.solve(starting, ending)) {
            System.out.println("Puzzle resuelto.");
        } else {
            System.out.println("No se pudo resolver el puzzle.");
        }
    }
}

