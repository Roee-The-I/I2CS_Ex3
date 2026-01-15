package assignments.Ex3;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {

    // edit this class below
    private int[][] map;
    private boolean _cyclicFlag = true;

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

    public Map(int[][] arr) {
        init(arr);
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
        if (arr == null || arr.length == 0 || arr[0] == null || arr[0].length == 0) {
            throw new RuntimeException("Invalid map");
        }

        int h = arr.length;        // rows (Y)
        int w = arr[0].length;     // columns (X)

        this.map = new int[w][h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                this.map[x][y] = arr[y][x];
            }
        }
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
        return this.map.length;
    }

    @Override
    public int getHeight() {
        return this.map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
            return -1;
        }
        return map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
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
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    @Override
    public void setCyclic(boolean cy) {
        _cyclicFlag = cy;
    }

    @Override
    public int fill(Pixel2D p, int new_v) {
        if (p == null || map == null || map.length == 0 || map[0].length == 0) {
            return 0;
        }

        int w = getWidth();
        int h = getHeight();

        int x0 = p.getX();
        int y0 = p.getY();

        if (x0 < 0 || x0 >= w || y0 < 0 || y0 >= h) {
            return 0;
        }

        int old = map[y0][x0];
        if (old == new_v) {
            return 0;
        }

        boolean[][] visited = new boolean[h][w];
        ArrayDeque<int[]> queue = new ArrayDeque<>();

        queue.add(new int[]{x0, y0});
        visited[y0][x0] = true;

        int count = 0;

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            int x = cur[0];
            int y = cur[1];

            if (map[y][x] != old) continue;

            map[y][x] = new_v;
            count++;

            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];

                if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;

                if (!visited[ny][nx] && map[ny][nx] == old) {
                    visited[ny][nx] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return count;
    }

    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        if (p1 == null || p2 == null) return null;
        int w = getWidth();
        int h = getHeight();
        int[][] map = getMap();
        boolean cyclic = isCyclic();
        int x1 = p1.getX(), y1 = p1.getY();
        int x2 = p2.getX(), y2 = p2.getY();
        if (!inside(x1, y1, w, h) || !inside(x2, y2, w, h)) return null;
        if (map[y1][x1] == obsColor || map[y2][x2] == obsColor) return null;
        boolean[][] visited = new boolean[h][w];
        int[][] px = new int[h][w];
        int[][] py = new int[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                px[y][x] = -1;
                py[y][x] = -1;
            }
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{x1, y1});
        visited[y1][x1] = true;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            int x = cur[0], y = cur[1];
            if (x == x2 && y == y2) break;
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];

                if (cyclic) {
                    nx = Math.floorMod(nx, w);
                    ny = Math.floorMod(ny, h);
                } else {
                    if (!inside(nx, ny, w, h)) continue;
                }

                if (visited[ny][nx]) continue;
                if (map[ny][nx] == obsColor) continue;

                visited[ny][nx] = true;
                px[ny][nx] = x;
                py[ny][nx] = y;
                queue.addLast(new int[]{nx, ny});
            }
        }
        if (!visited[y2][x2]) return null;
        ArrayList<Pixel2D> rev = new ArrayList<>();
        for (int x = x2, y = y2; x != -1; ) {
            rev.add(new Index2D(x, y));
            int nx = px[y][x];
            int ny = py[y][x];
            x = nx;
            y = ny;
        }
        Pixel2D[] path = new Pixel2D[rev.size()];
        for (int i = 0; i < rev.size(); i++) {
            path[i] = rev.get(rev.size() - 1 - i);
        }
        return path;
    }

    private static boolean inside(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
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
                assignments.Ex3.Index2D np = new assignments.Ex3.Index2D(nx, ny);
                if (getPixel(np) == obsColor) continue;
                if (dist.getPixel(np) != -1) continue;
                dist.setPixel(np, d + 1);
                q.addLast(np);
            }
        }
        return dist;
    }

    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        int w = getWidth();
        int h = getHeight();
        int[][] the_map = getMap();
        int p1X = p1.getX();
        int p1Y = p1.getY();
        int p2X = p2.getX();
        int p2Y = p2.getY();
        if (!inside(p1X, p1Y, w, h) || !inside(p2X, p2Y, w, h)) return null;
        if (the_map[p1Y][p1X] == obsColor || the_map[p2Y][p2X] == obsColor) return null;
        boolean[][] vis = new boolean[h][w];
        int[][] px = new int[h][w];
        int[][] py = new int[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                px[y][x] = -1;
                py[y][x] = -1;
            }
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{p1X, p1Y});
        vis[p1Y][p1X] = true;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            int x = cur[0];
            int y = cur[1];
            if (x == p2X && y == p2Y) break;
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];
                if (cyclic) {
                    nx = Math.floorMod(nx, w);
                    ny = Math.floorMod(ny, h);
                } else {
                    if (!inside(nx, ny, w, h)) continue;
                }
                if (vis[ny][nx]) continue;
                if (the_map[ny][nx] == obsColor) continue;
                vis[ny][nx] = true;
                px[ny][nx] = x;
                py[ny][nx] = y;
                queue.addLast(new int[]{nx, ny});
            }
        }
        if (!vis[p2Y][p2X]) return null;
        ArrayList<Pixel2D> rev = new ArrayList<>();
        for (int x = p2X, y = p2Y; x != -1; ) {
            rev.add(new Index2D(x, y));
            int nx = px[y][x];
            int ny = py[y][x];
            x = nx;
            y = ny;
        }
        Pixel2D[] ans = new Pixel2D[rev.size()];
        for (int i = 0; i < rev.size(); i++) {
            ans[i] = rev.get(rev.size() - 1 - i);
        }
        return ans;
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {

        int w = getWidth();
        int h = getHeight();
        Map dist = new Map(w, h, -1);

        if (start == null) return dist;

        int sx = start.getX();
        int sy = start.getY();

        if (sx < 0 || sx >= w || sy < 0 || sy >= h) return dist;
        if (getPixel(start) == obsColor) return dist;

        ArrayDeque<Index2D> q = new ArrayDeque<>();
        dist.setPixel(start, 0);
        q.addLast(new Index2D(sx, sy));

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!q.isEmpty()) {
            Index2D p = q.removeFirst();
            int d = dist.getPixel(p);

            for (int k = 0; k < 4; k++) {
                int nx = p.getX() + dx[k];
                int ny = p.getY() + dy[k];

                if (cyclic) {
                    nx = Math.floorMod(nx, w);
                    ny = Math.floorMod(ny, h);
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                }

                Index2D np = new Index2D(nx, ny);

                if (getPixel(np) == obsColor) continue;
                if (dist.getPixel(np) != -1) continue;

                dist.setPixel(np, d + 1);
                q.addLast(np);
            }
        }
        return dist;
    }
}
