package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.PacmanGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Ex3AlgoTests {
    @Test
    public void testMoveReturnsValidDirection() {
        Game game = new Game();
        Ex3Algo algo = new Ex3Algo();

        int dir = algo.move(game);

        boolean valid = dir == PacmanGame.UP || dir == PacmanGame.LEFT || dir == PacmanGame.RIGHT || dir == PacmanGame.STAY;
        assertTrue(valid, "Direction must be a valid PacmanGame constant");
    }

    @Test
    public void testMoveDoesNotCrash() {
        Game game = new Game();
        Ex3Algo algo = new Ex3Algo();

        assertDoesNotThrow(() -> algo.move(game));
    }
}
