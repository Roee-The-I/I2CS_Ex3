package assignments.Ex3.Mygame;

import assignments.Ex3.Map2D;

import java.util.List;

public class PacmanRenderer {
    private int row, col;
    private long superModeStartTime = 0;
    private static final long SUPER_DURATION = 8000;
    private MyPackman brain;

    public PacmanRenderer(int row, int col) {
        this.row = row;
        this.col = col;
        this.brain = new MyPackman();
    }

    public void move(Board board, Map2D map, List<Ghost> ghosts) {
        int[] nextPos = brain.Move(this.row, this.col, map, ghosts, isSuper());
        this.row = nextPos[0];
        this.col = nextPos[1];

        updateGameState(board, map, ghosts);
    }

    private void updateGameState(Board board, Map2D map, List<Ghost> ghosts) {
        int cellValue = map.getPixel(this.col, this.row);

        if (cellValue == 1) {
            return;
        }
        if (cellValue == 2 || cellValue == 3) {
            if (cellValue == 3) {
                this.superModeStartTime = System.currentTimeMillis();
            }
            map.setPixel(this.col, this.row, 0);
            board.setCell(this.col, this.row, 0);
        }
        for (Ghost g : ghosts) {
            if (g.isAlive() && g.getY() == this.row && g.getX() == this.col) {
                if (isSuper()) {
                    g.die();
                } else {
                    System.out.println("Game Over!");
                    System.exit(0);
                }
            }
        }
    }

    public void draw(Board board) {
        double cellW = 1.0 / board.getWidth();
        double cellH = 1.0 / board.getHeight();
        double px = col * cellW + cellW / 2;
        double py = 1 - (row * cellH + cellH / 2);
        double size = Math.min(cellW, cellH) * 0.8;

        if (isSuper()) {
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.setPenRadius(0.01);
            StdDraw.circle(px, py, size / 1.5);
        } else {
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.setPenRadius(0.01);
            StdDraw.circle(px, py, size / 1.5);
        }
    }

    public boolean isSuper() {
        return (System.currentTimeMillis() - superModeStartTime < SUPER_DURATION);
    }
}
