package assignments.Ex3.Mygame;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import assignments.Ex3.Mygame.StdDraw ;
public class Ghost {
    private int x, y;
    private final int startX, startY;
    private Color color;
    private String name;
    private boolean isAlive = true;
    private long deathTime = 0;
    private static final long RESPAWN_TIME = 3000; // 5 seconds in milliseconds
    private Random rand = new Random();
    public Ghost(int startX, int startY, Color color, String name) {
        this.x = startX;
        this.y = startY;
        this.startX = startX; // Save for respawn
        this.startY = startY; // Save for respawn
        this.color = color;
        this.name = name;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return isAlive; }
    public void draw(int boardWidth, int boardHeight) {
        if (!isAlive) return;

        double cellW = 1.0 / boardWidth;
        double cellH = 1.0 / boardHeight;
        double px = x * cellW + cellW / 2;
        double py = 1 - (y * cellH + cellH / 2);

        StdDraw.setPenColor(this.color);
        double r = Math.min(cellW, cellH) * 0.4;
        StdDraw.filledCircle(px, py, r);
        StdDraw.filledRectangle(px, py - r/2, r, r/2);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 16));
        StdDraw.text(px, py + r + 0.02, this.name);
    }
    public void die() {
        this.isAlive = false;
        this.deathTime = System.currentTimeMillis();
    }
    public void move(Board board) {
        if (!isAlive) {
            checkRespawn();
            return;
        }

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        int dir = rand.nextInt(4);
        for (int i = 0; i < 4; i++) {
            int nextDir = (dir + i) % 4;
            int nextCol = x + dx[nextDir];
            int nextRow = y + dy[nextDir];

            if (nextRow >= 0 && nextRow < board.getHeight() &&
                    nextCol >= 0 && nextCol < board.getWidth()) {

                if (board.getCell(nextCol, nextRow) != 1) {
                    this.x = nextCol;
                    this.y = nextRow;
                    return;
                }
            }
        }
    }

    private void checkRespawn() {
        if (System.currentTimeMillis() - deathTime >= RESPAWN_TIME) {
            this.isAlive = true;
            this.x = startX;
            this.y = startY;
        }
    }
}
