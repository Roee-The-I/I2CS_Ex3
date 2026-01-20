package assignments.Ex3.Mygame;

import assignments.Ex3.Map2D;

import java.util.List;

public class PacmanRenderer {
    private int row, col;
    private long superModeStartTime = 0;
    private static final long SUPER_DURATION = 8000;
    private MyPackman brain;
    public void move(Board board, Map2D map, List<Ghost> ghosts) {
        int[] nextPos = brain.Move(this.row, this.col, map, ghosts, isSuper());

        this.row = nextPos[0];
        this.col = nextPos[1];

        updateGameState(board, map, ghosts);
    }

    private void updateGameState(Board board, Map2D map, List<Ghost> ghosts) {
        int cellValue = map.getPixel(this.row, this.col);
        if (cellValue > 1) {
            // Value 3 represents super food
            if (cellValue == 3) this.superModeStartTime = System.currentTimeMillis();
            map.setPixel(this.row, this.col, 0);
            board.setCell(this.col, this.row, 0);
        }

        for (Ghost g : ghosts) {
            if (g.isAlive() && g.getY() == this.row && g.getX() == this.col) {
                if (isSuper()) {
                    g.die();
                } else {
                    System.out.println("GAME OVER!");
                }
            }
        }
    }

    public boolean isSuper() {
        return (System.currentTimeMillis() - superModeStartTime < SUPER_DURATION);
    }
}
