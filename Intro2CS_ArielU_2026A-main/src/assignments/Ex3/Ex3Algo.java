package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 * <p>
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo {
    private int _count;
    private int steps;
    private int lastDir = Game.STAY;
    private Index2D lastPos = null;
    public int BLUE, PINK, GREEN;

    public Ex3Algo() {
        _count = 0;
    }

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
        int[][] gameMap = game.getGame(0);
        Map map = new Map(gameMap);
        map.setCyclic(game.isCyclic());
        Pixel2D me = new Index2D(posToIndex2D(game.getPos(0)));

        if (steps == 0) {
            BLUE = Game.getIntColor(Color.BLUE, 0);
            PINK = Game.getIntColor(Color.PINK, 0);
            GREEN = Game.getIntColor(Color.GREEN, 0);
        }
        GhostCL[] ghosts = game.getGhosts(0);
        double[][] danger = CreateDangerMap(map, ghosts);
        int bestDir = Game.STAY;
        double bestScore = Double.NEGATIVE_INFINITY;
        Map2D depthMap = map.allDistance(me, BLUE);
        for (int dir : new int[]{Game.UP, Game.RIGHT, Game.DOWN, Game.LEFT}) {
            Pixel2D next = NextCell(me, dir, map);
            if (!isValid(next, gameMap)) continue;
            double score = evaluate(next, map, gameMap, danger);
            double future = depthMap.getPixel(next.getX(), next.getY());
            if (future != -1) score += 0.3 * future;
            if (dir == lastDir) score += 20;
            if (score > bestScore) {
                bestScore = score;
                bestDir = dir;
            }
        }
        if (bestDir == Game.STAY) {
            for (int d : new int[]{Game.UP, Game.RIGHT, Game.DOWN, Game.LEFT}) {
                if (isValid(NextCell(me, d, map), gameMap)) {
                    bestDir = d;
                    break;
                }
            }
        }

        lastDir = bestDir;
        steps++;
        return bestDir;
    }
    private Index2D getPacmanIndex(Game game) {
        String pos = game.getPos(0);
        pos = pos.replace("(", "").replace(")", "");
        String[] p = pos.split(",");
        int x = Integer.parseInt(p[0]);
        int y = Integer.parseInt(p[1]);
        return new Index2D(x, y);
    }

    private Index2D getGhostIndex(Game game) {

        GhostCL[] gs = game.getGhosts(0);
        if (gs == null || gs.length == 0 || gs[0] == null) return null;

        String gpos = gs[0].getPos(0);
        if (gpos == null) return null;

        gpos = gpos.replace("(", "").replace(")", "").trim();
        String[] p = gpos.split(",");

        if (p.length < 2) return null;

        int x = Integer.parseInt(p[0].trim());
        int y = Integer.parseInt(p[1].trim());

        return new Index2D(x, y);
    }
    public double evaluate(Pixel2D pos, Map map, int[][] gamemap, double[][] danger) {
        double score = 0;
        int x = pos.getX(), y = pos.getY();
        double ghostDist = danger[x][y];

        if (ghostDist <= 1.1) return -10000000.0;
        if (ghostDist <= 2.1) score -= 500000.0;
        if (ghostDist <= 3.1) score -= 100000.0;

        int safeSpace = countSafeSpace(pos, map, gamemap, danger, 15);
        score += safeSpace * 2000;

        Map2D distMap = map.allDistance(pos, BLUE);
        Pixel2D pink = Close(gamemap, distMap, PINK);

        if (pink != null) {
            double d = distMap.getPixel(pink.getX(), pink.getY());
            score += 200000.0 / (d + 1);
        } else {
            score += ghostDist * 5000;
        }

        if (gamemap[x][y] == PINK) score += 10000;

        return score;
    }
    public int countSafeSpace(Pixel2D start, Map map, int[][] board, double[][] danger, int limit) {
        Queue<Pixel2D> q = new LinkedList<>();
        java.util.Map<String, Integer> dist = new HashMap<>();
        q.add(start);
        dist.put(Cell(start), 0);
        int count = 0;
        while (!q.isEmpty() && count < limit) {
            Pixel2D cur = q.poll();
            int d = dist.get(Cell(cur));
            count++;
            for (int dir : new int[]{0,1,2,3}) {
                Pixel2D n = nextCell(cur, map.getMap(), dir);
                if (!isValid(n, board) || dist.containsKey(Cell(n))) continue;
                if (danger[n.getX()][n.getY()] <= d + 1) continue;
                dist.put(Cell(n), d + 1);
                q.add(n);
            }
        }
        return count;
    }

    public double[][] CreateDangerMap(Map map, GhostCL[] ghosts) {
        int w = map.getMap().length, h = map.getMap()[0].length;
        double[][] danger = new double[w][h];
        for (double[] r : danger) Arrays.fill(r, 99.0);

        if (ghosts != null && ghosts.length > 0) {
            for (int i = 0; i < ghosts.length; i++) {
                if (ghosts[i].remainTimeAsEatable(i) > 3.0) continue;
                Pixel2D gp = new Index2D(posToIndex2D(ghosts[i].getPos(i)));
                Map2D dist = map.allDistance(gp, BLUE);
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        double d = dist.getPixel(x, y);
                        if (d != -1) danger[x][y] = Math.min(danger[x][y], d);
                    }
                }
            }
        } else {
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    if (map.getMap()[x][y] < 0) {
                        danger[x][y] = 0;
                    }
                }
            }
        }

        return danger;
    }

    private Index2D posToIndex2D(String pos) {
        if (pos == null) return null;

        pos = pos.replace("(", "").replace(")", "");
        String[] parts = pos.split(",");

        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());

        return new Index2D(x, y);
    }
    public Pixel2D Close(int[][] map, Map2D dist, int color) {
        Pixel2D best = null; double min = Double.MAX_VALUE;
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (map[i][j] == color && dist.getPixel(i, j) != -1 && dist.getPixel(i, j) < min) {
                    min = dist.getPixel(i, j); best = new Index2D(i, j);
                }
        return best;
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

                if (map[x][y] == FOOD) {

                    Index2D food = new Index2D(x, y);

                    Pixel2D[] path = m.shortestPath(pac, food, WALL, cyclic);

                    if (path != null) {
                        int len = path.length - 1;
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


    private boolean isValid(Pixel2D p, int[][] board) {
        if (p.getX() < 0 || p.getX() >= board.length || p.getY() < 0 || p.getY() >= board[0].length) return false;
        return board[p.getX()][p.getY()] != BLUE && !GhostArea(p, board);
    }

    private int opposite(int dir) {
            if (dir == Game.UP) return Game.DOWN;
            if (dir == Game.DOWN) return Game.UP;
            if (dir == Game.LEFT) return Game.RIGHT;
            if (dir == Game.RIGHT) return Game.LEFT;
            return Game.STAY;
    }

    public Pixel2D NextCell(Pixel2D p, int dir, Map map) {
        int x = p.getX(), y = p.getY();
        if (dir == Game.UP) y++;
        else if (dir == Game.DOWN) y--;
        else if (dir == Game.LEFT) x--;
        else if (dir == Game.RIGHT) x++;
        int w = map.getMap().length, h = map.getMap()[0].length;
        return new Index2D((x + w) % w, (y + h) % h);
    }
    public String Cell(Pixel2D p) { return p.getX() + "," + p.getY(); }


    private boolean isWall(int[][] map, int x, int y) {
        if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) return true;
        int WALL = Game.getIntColor(java.awt.Color.BLUE, 0);
        return map[x][y] == WALL;
    }

    private Pixel2D nextCell(Pixel2D pixel, int[][] map, int dir) {
        int x = pixel.getX();
        int y = pixel.getY();

        if (dir == Game.RIGHT) x++;
        if (dir == Game.LEFT) x--;
        if (dir == Game.UP) y--;
        if (dir == Game.DOWN) y++;

        int w = map.length;
        int h = map[0].length;

        return new Index2D(Math.floorMod(x, w), Math.floorMod(y, h));
    }

    private boolean GhostArea(Pixel2D p, int[][] map) {
        int mx = map.length / 2, my = map[0].length / 2;
        return Math.abs(p.getX() - mx) < 3 && Math.abs(p.getY() - my) < 3 && map[p.getX()][p.getY()] == 0;
    }
}