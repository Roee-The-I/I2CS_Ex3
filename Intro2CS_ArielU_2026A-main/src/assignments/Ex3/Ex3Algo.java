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

        int[][] map = game.getGame(0);
        Map2D m = new Map(map);

        Index2D pac = getPacmanIndex(game);

        Index2D target = findClosestFood(m, map, pac);
        if (target != null) {

            int WALL = Game.getIntColor(Color.BLUE, 0); // או הקבוע שלכם
            boolean cyclic = false;

            Pixel2D[] path = m.shortestPath(pac, target, WALL, cyclic);

            if (path != null && path.length > 1) {
                return directionFromTo(pac, (Index2D) path[1]);
            }
        }

        return fallbackMove(map, pac);
    }
    private Index2D getPacmanIndex(Game game) {
        String pos = game.getPos(0);
        pos = pos.replace("(", "").replace(")", "");
        String[] p = pos.split(",");
        int x = Integer.parseInt(p[0]);
        int y = Integer.parseInt(p[1]);
        return new Index2D(x, y);
    }
    private Index2D findClosestFood(Map2D m, int[][] map, Index2D pac) {

        int FOOD = Game.getIntColor(Color.PINK, 0);
        int WALL = Game.getIntColor(Color.BLUE, 0);
        boolean cyclic = false;

        int bestLen = Integer.MAX_VALUE;
        Index2D best = null;

        int h = map.length;
        int w = map[0].length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                if (map[y][x] == FOOD) {

                    Index2D food = new Index2D(x, y);

                    Pixel2D[] path = m.shortestPath(pac, food, WALL, cyclic);

                    if (path != null) {
                        int len = path.length - 1; // מספר צעדים
                        if (len < bestLen) {
                            bestLen = len;
                            best = food;
                        }
                    }
                }
            }
        }
        return best;
    }
    private int directionFromTo(Index2D a, Index2D b) {
        if (b.getX() > a.getX()) return Game.RIGHT;
        if (b.getX() < a.getX()) return Game.LEFT;
        if (b.getY() > a.getY()) return Game.DOWN;
        if (b.getY() < a.getY()) return Game.UP;
        return Game.STAY;
    }
    private int fallbackMove(int[][] map, Index2D pac) {

        int WALL = Game.getIntColor(Color.BLUE, 0);
        int x = pac.getX();
        int y = pac.getY();

        if (x + 1 < map.length && map[x + 1][y] != WALL) return Game.RIGHT;
        if (y - 1 >= 0 && map[x][y - 1] != WALL) return Game.UP;
        if (x - 1 >= 0 && map[x - 1][y] != WALL) return Game.LEFT;
        if (y + 1 < map[0].length && map[x][y + 1] != WALL) return Game.DOWN;

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
    private boolean isWall(int[][] map, int x, int y) {
        if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) return true;
        int WALL = Game.getIntColor(java.awt.Color.BLUE, 0);
        return map[x][y] == WALL;
    }
    private int[] nextCell(int x, int y, int dir) {
        if (dir == Game.RIGHT) return new int[]{x + 1, y};
        if (dir == Game.LEFT)  return new int[]{x - 1, y};
        if (dir == Game.UP)    return new int[]{x, y - 1};
        if (dir == Game.DOWN)  return new int[]{x, y + 1};
        return new int[]{x, y};
    }
}