package assignments.Ex3.Mygame;

import assignments.Ex3.Index2D;
import assignments.Ex3.Map;
import assignments.Ex3.Map2D;
import assignments.Ex3.Pixel2D;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import  java.util.List;
public class MyPackman {
    private int _count;
    private int steps;
    private Index2D lastPos = null;
    public int BLUE, PINK, GREEN;
    private Index2D currentTarget = null;
    private int lastDir = -1;
    private Pixel2D prevPrevPos = null;
    public double evaluate(Pixel2D pos, Map2D map, double[][] dangerMap, boolean isSuper) {
        double score = 0;
        double distToGhost = dangerMap[pos.getX()][pos.getY()];

        if (isSuper) {
            if (distToGhost <= 5.1) score += (250000.0 / (distToGhost + 1));
        } else {
            if (distToGhost <= 1.1) return -999999.0;
            if (distToGhost <= 3.1) score -= 30000.0;
        }

        Map2D distToFoodMap = map.allDistance(pos, 1);
        Pixel2D closestFood = findClosestFood(map, distToFoodMap);
        if (closestFood != null) {
            double d = distToFoodMap.getPixel(closestFood.getX(), closestFood.getY());
            score += 15000.0 / (d + 1);
        }

        int cell = map.getPixel(pos.getX(), pos.getY());
        if (cell == 2) score += 5000;
        if (cell == 3) score += 60000;

        return score;
    }

    public int[] Move(int curRow, int curCol, Map2D map, List ghosts, boolean isSuper) {
        Pixel2D prev = lastPos;
        Pixel2D me = new Index2D(curRow, curCol);
        double[][] dangerMap = buildDangerMap(map, ghosts);

        int bestDir = -1;
        double maxScore = Double.NEGATIVE_INFINITY;

        for (int dir = 0; dir < 4; dir++) {
            Pixel2D next = getCell(me, dir, map);
            if (next.getX() == me.getX() && next.getY() == me.getY()) continue;
            if (map.getPixel(next.getY(), next.getX()) == 1) continue;
            if (dir == opposite(lastDir)) continue;

            double score = evaluate(next, map, dangerMap, isSuper);
            if (dir == lastDir) score += 20;

            if (score > maxScore) {
                maxScore = score;
                bestDir = dir;
            }
        }

        if (bestDir != -1) {
            lastDir = bestDir;
            Pixel2D finalPos = getCell(me, bestDir, map);
            return new int[]{finalPos.getX(), finalPos.getY()};
        }
        for (int dir = 0; dir < 4; dir++) {
            if (lastDir != -1 && dir == opposite(lastDir)) continue;
            Pixel2D next = getCell(me, dir, map);
            if (next.getX() != me.getX() || next.getY() != me.getY()) {
                lastDir = dir;
                return new int[]{next.getX(), next.getY()};
            }
            if (prevPrevPos != null &&
                    next.getX() == prevPrevPos.getX() &&
                    next.getY() == prevPrevPos.getY()) {
                continue;
            }
        }
        prevPrevPos = lastPos;
        lastPos = new Index2D(curRow, curCol);
        return new int[]{curRow, curCol};
    }

    private Pixel2D getCell(Pixel2D p, int dir, Map2D map) {
        int r = p.getX(), c = p.getY();
        if (dir == 0) r--;
        else if (dir == 1) c++;
        else if (dir == 2) r++;
        else if (dir == 3) c--;
        if (r < 0 || r >= map.getHeight() || c < 0 || c >= map.getWidth()) {
            return p;
        }
        if (map.getPixel(c, r) == 1) {
            return p;
        }
        return new Index2D(r, c);
    }
    private int opposite(int dir) {
        return (dir + 2) % 4;
    }

    public Pixel2D Close(int[][] map, Map2D dist, int color) {
        Pixel2D best = null;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (map[i][j] == color && dist.getPixel(i, j) != -1 && dist.getPixel(i, j) < min) {
                    min = dist.getPixel(i, j);
                    best = new Index2D(i, j);
                }
        return best;
    }

    public double[][] buildDangerMap(Map2D map, List<Ghost> ghosts) {
        int h = map.getHeight(), w = map.getWidth();
        double[][] danger = new double[h][w];
        for (int i = 0; i < h; i++) for (int j = 0; j < w; j++) danger[i][j] = 99.0;

        for (Ghost g : ghosts) {
            if (!g.isAlive()) continue;
            Pixel2D ghostPos = new Index2D(g.getY(), g.getX());
            Map2D distFromGhost = map.allDistance(ghostPos, 1);
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    double d = distFromGhost.getPixel(i, j);
                    if (d != -1 && d < danger[i][j]) danger[i][j] = d;
                }
            }
        }
        return danger;
    }


    private boolean isValid(Pixel2D p, int[][] board) {
        if (p.getX() < 0 || p.getX() >= board.length || p.getY() < 0 || p.getY() >= board[0].length) return false;
        return board[p.getX()][p.getY()] != BLUE && !GhostArea(p, board);
    }

    private boolean GhostArea(Pixel2D p, int[][] map) {
        int mx = map.length / 2, my = map[0].length / 2;
        return Math.abs(p.getX() - mx) < 3 && Math.abs(p.getY() - my) < 3 && map[p.getX()][p.getY()] == 0;
    }

    private Pixel2D findClosestFood(Map2D map, Map2D distMap) {
        Pixel2D closest = null;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int val = map.getPixel(i, j);
                double d = distMap.getPixel(i, j);
                if ((val == 2 || val == 3) && d >= 0 && d < minDist) {
                    minDist = d;
                    closest = new Index2D(i, j);
                }
            }
        }
        return closest;
    }
}
