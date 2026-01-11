package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
    private int lastDir = Game.STAY;
    public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;
		int dir = randomDir();
		return dir;
	}
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
    public int move(Game game) {
        String pos = game.getPos(0);
        pos = pos.replace("(", "").replace(")", "");
        String[] p = pos.split(",");
        int x = Integer.parseInt(p[0]);
        int y = Integer.parseInt(p[1]);
        int[][] map = game.getGame(0);
        int w = map.length;
        int h = map[0].length;
        int WALL = Game.getIntColor(Color.BLUE, 0);
        int FOOD = Game.getIntColor(Color.PINK, 0);
        int bestDir = Game.STAY;
        int[] dirs = {Game.RIGHT, Game.UP, Game.LEFT, Game.DOWN};
        for (int dir : dirs) {
            int nx = x, ny = y;
            if (dir == Game.RIGHT) nx++;
            if (dir == Game.LEFT)  nx--;
            if (dir == Game.UP)    ny--;
            if (dir == Game.DOWN)  ny++;
            if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
            if (map[nx][ny] == WALL) continue;
            if (map[nx][ny] == FOOD) {
                lastDir = dir;
                return dir;
            }
            if (bestDir == Game.STAY && dir != opposite(lastDir)) {
                bestDir = dir;
            }
        }
        if (bestDir != Game.STAY) {
            lastDir = bestDir;
            return bestDir;
        }
        return Game.STAY;
    }
    private boolean isValid(Game game, int[][] map, int x, int y, int dir, int WALL) {
        int newx = x, newy = y;
        if (dir == Game.RIGHT) newx++;
        if (dir == Game.LEFT)  newx--;
        if (dir == Game.UP)    newy--;
        if (dir == Game.DOWN)  newy++;
        return newx >= 0 && newy >= 0 && newx < map.length && newy < map[0].length && map[newx][newy] != WALL;
    }
    private int opposite(int dir) {
        if (dir == Game.UP) return Game.DOWN;
        if (dir == Game.DOWN) return Game.UP;
        if (dir == Game.LEFT) return Game.RIGHT;
        if (dir == Game.RIGHT) return Game.LEFT;
        return Game.STAY;
    }
}