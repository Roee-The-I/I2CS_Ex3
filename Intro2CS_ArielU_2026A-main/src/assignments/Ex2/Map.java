package assignments.Ex2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable {

    // edit this class below
    private int[][] map;

    /**
     * Constructs a w*h 2D raster map with an init value v.
     *
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     *
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     *
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }

    @Override
    public void init(int w, int h, int v) {
        if (w <= 0 || h <= 0) {
            throw new RuntimeException("Invalid dimensions");
        }
        int[][] newMap = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                newMap[i][j] = v;
            }
        }
        this.map = newMap;
    }

    @Override
    public void init(int[][] arr) {
        if (arr == null || arr.length == 0) {
            throw new RuntimeException("Invalid dimensions");
        }
        if (arr[0] == null || arr[0].length == 0) {
            throw new RuntimeException("Invalid array");
        }
        int h = arr.length;
        int w = arr[0].length;
        int[][] ans = new int[w][h];
        for (int i = 0; i < w; i++) {
            if (arr[i] == null || arr[i].length != w) {
                throw new RuntimeException("Ragged or null row");
            }
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                ans[i][j] = arr[i][j];
            }
        }
        this.map = ans;
    }

    @Override
    public int[][] getMap() {
        int w = this.map.length;
        int h = this.map[0].length;
        int[][] ans = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                ans[i][j] = this.map[i][j];
            }
        }
        return ans;
    }

    @Override
    public int getWidth() {
        return map.length;
    }

    @Override
    public int getHeight() {
        return map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        return map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        if (p == null) {
            throw new RuntimeException("Invalid dimensions");
        }
        return map[p.getX()][p.getY()];
    }

    @Override
    public void setPixel(int x, int y, int v) {
        map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        if (p == null) {
            throw new RuntimeException("Invalid dimensions");
        }
        map[p.getX()][p.getY()] = v;
    }

    @Override
    public boolean isInside(Pixel2D p) {
        if (p == null) return false;
        int x = p.getX();
        int y = p.getY();
        return x >= 0 && x < map.length &&
                y >= 0 && y < map[0].length;
    }

    @Override
    public boolean sameDimensions(Map2D p) {
        if (p == null) {
            throw new RuntimeException("map is Null");
        }
        return map.length == p.getMap().length && map[0].length == p.getMap()[0].length;
    }

    @Override
    public void addMap2D(Map2D p) {
        if (!sameDimensions(p)) {
            return;
        }
        var mapParameter = p.getMap();
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                map[i][j] += mapParameter[i][j];
            }
        }
    }

    @Override
    public void mul(double scalar) {
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[0].length; j++) {
                this.map[i][j] = (int) (this.map[i][j] * scalar);
            }
        }
    }

    @Override
    public void rescale(double sx, double sy) {
        int oldwidth = this.map.length;
        int oldhight = this.map[0].length;
        int newwidth = (int) (this.map.length * sx);
        int newhight = (int) (this.map[0].length * sy);
        int[][] newMap = new int[newwidth][newhight];
        for (int i = 0; i < newwidth; i++) {
            for (int j = 0; j < newhight; j++) {
                int oldI = (int) (i / sx);
                int oldJ = (int) (j / sy);
                if (oldI >= oldwidth) oldI = oldwidth - 1;
                if (oldJ >= oldhight) oldJ = oldhight - 1;
                newMap[i][j] = map[oldI][oldJ];
            }
        }
        this.map = newMap;
    }

    @Override
    public void drawCircle(Pixel2D center, double rad, int color) {
        int center_x = center.getX();
        int center_y = center.getY();
        int radius = (int) (rad * rad);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int dx = i - center_x;
                int dy = j - center_y;
                if (dx * dx + dy * dy <= radius) {
                    map[i][j] = color;
                }
            }
        }
    }


    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        int x1 = p1.getX();
        int x2 = p2.getX();
        int y1 = p1.getY();
        int y2 = p2.getY();
        if (x1 == x2 && y1 == y2) {
            map[x1][y1] = color;
            return;
        }
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        if (dx >= dy) {
            if (x1 > x2) {
                int tx = x1;
                x1 = x2;
                x2 = tx;
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            double m = (x2 == x1) ? 0.0 : ((double) (y2 - y1)) / (double) (x2 - x1);
            for (int x = x1; x <= x2; x++) {
                double yReal = y1 + m * (x - x1);
                int y = (int) Math.round(yReal);
                map[x][y] = color;
            }
        } else {
            if (y1 > y2) {
                int tx = x1;
                x1 = x2;
                x2 = tx;
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            double m = (y2 == y1) ? 0.0 : ((double) (x2 - x1)) / (double) (y2 - y1);
            for (int y = y1; y <= y2; y++) {
                double xReal = x1 + m * (y - y1);
                int x = (int) Math.round(xReal);
                map[x][y] = color;
            }
        }
    }

    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        int x1 = p1.getX();
        int x2 = p2.getX();
        int y1 = p1.getY();
        int y2 = p2.getY();
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                map[x][y] = color;
            }
        }
    }

    @Override
    public boolean equals(Object ob) {
        boolean ans = false;
        if (this == ob) {
            ans = true;
            return ans;
        }
        if (ob == null || !(ob instanceof Map2D)) {
            ans = false;
            return ans;
        }
        Map2D map2D = (Map2D) ob;

        if (this.getWidth() != map2D.getWidth() || this.getHeight() != map2D.getHeight()) {
            ans = false;
            return ans;
        }
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (this.getPixel(x, y) != map2D.getPixel(x, y)) {
                    ans = false;
                    return ans;
                }
            }
        }
        ans = true;
        return ans;
    }

    @Override
    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     */
    public int fill(Pixel2D xy, int new_v, boolean cyclic) {
        if (xy == null || map == null || map.length == 0 || map[0].length == 0) return 0;
        int w = getWidth();
        int h = getHeight();
        int x0 = xy.getX();
        int y0 = xy.getY();
        if (!cyclic) {
            if (x0 < 0 || x0 >= w || y0 < 0 || y0 >= h) return 0;
        } else {
            x0 = Math.floorMod(x0, w);
            y0 = Math.floorMod(y0, h);
        }
        int old = map[y0][x0];
        if (old == new_v) return 0;
        boolean[][] visited = new boolean[h][w];
        ArrayDeque<int[]> pixelsQueue = new ArrayDeque<>();
        pixelsQueue.add(new int[]{x0, y0});
        visited[y0][x0] = true;
        int count = 0;
        while (!pixelsQueue.isEmpty()) {
            int[] cur = pixelsQueue.removeFirst();
            int x = cur[0], y = cur[1];
            if (map[y][x] != old) continue;
            map[y][x] = new_v;
            count++;
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];
                if (cyclic) {
                    nx = Math.floorMod(nx, w);
                    ny = Math.floorMod(ny, h);
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                }
                if (!visited[ny][nx] && map[ny][nx] == old) {
                    visited[ny][nx] = true;
                    pixelsQueue.add(new int[]{nx, ny});
                }
            }
        }
        return count;
    }

    @Override
    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        int w = getWidth();
        int h = getHeight();
        int[][] the_map = getMap();
        int p1X = p1.getX();
        int p1Y = p1.getY();
        int p2X = p2.getX();
        int p2Y = p2.getY();
        if (!inside(p1X, p1Y, w, h) || !inside(p2X, p2Y, w, h)) return null;
        if (the_map[p1X][p1Y] == obsColor || the_map[p2X][p2Y] == obsColor) return null;
        boolean[][] vis = new boolean[w][h];
        int[][] px = new int[h][w];
        int[][] py = new int[h][w];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++) {
                px[i][j] = -1;
                py[i][j] = -1;
            }
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{p1Y, p1X});
        vis[p1Y][p1X] = true;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            int x = cur[0], y = cur[1];
            if (x == p2X && y == p2Y) break;
            for (int k = 0; k < 4; k++) {
                int new_x = x + dx[k];
                int new_y = y + dy[k];
                if (cyclic) {
                    new_x = (new_x % w + w) % w;
                    new_y = (new_y % h + h) % h;
                } else {
                    if (!inside(new_x, new_y, w, h)) continue;
                }
                if (vis[new_y][new_x]) continue;
                if (the_map[new_y][new_x] == obsColor) continue;
                vis[new_y][new_x] = true;
                px[new_y][new_x] = x;
                py[new_y][new_x] = y;
                queue.addLast(new int[]{new_y, new_x});
            }
        }
        if (!vis[p2X][p2Y]) return null;
        ArrayList<Pixel2D> rev = new ArrayList<>();
        for (int x = p2X, y = p2Y; x != -1; ) {
            rev.add(new Index2D(x, y));
            int nx = px[x][y];
            int ny = py[x][y];
            x = nx;
            y = ny;
        }
        Pixel2D[] ans = new Pixel2D[rev.size()];
        for (int i = 0; i < rev.size(); i++) ans[i] = rev.get(rev.size() - 1 - i);
        return ans;
    }

    private static boolean inside(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        int w = getWidth();
        int h = getHeight();
        Map dist = new Map(w, h, -1);
        if (start == null) return dist;
        if (start.getX() < 0 || start.getX() >= w || start.getY() < 0 || start.getY() >= h) return dist;
        if (getPixel(start) == obsColor) return dist;
        ArrayDeque<Index2D> q = new ArrayDeque<>();
        dist.setPixel(start, 0);
        q.addLast((Index2D) (start));
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        while (!q.isEmpty()) {
            Index2D p = q.removeFirst();
            int d = dist.getPixel(p);
            for (int k = 0; k < 4; k++) {
                int nx = p.getX() + dx[k];
                int ny = p.getY() + dy[k];
                if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                Index2D np = new Index2D(nx, ny);
                if (getPixel(np) == obsColor) continue;
                if (dist.getPixel(np) != -1) continue;
                dist.setPixel(np, d + 1);
                q.addLast(np);
            }
        }
        return dist;
    }
    ////////////////////// Private Methods ///////////////////////

}
