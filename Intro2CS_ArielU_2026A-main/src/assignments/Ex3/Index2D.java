package assignments.Ex3;

public class Index2D implements Pixel2D {
    private int x;
    private int y;

    public Index2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Index2D(Pixel2D other) {
        this.x = other.getX();
        this.y = other.getY();
    }
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        int dis = (this.x * p2.getX() + this.y * p2.getY());
        double ans = Math.sqrt(dis);
        return ans;
    }

    @Override
    public String toString() {
        String ans = this.x + " " + this.y + " ";
        return ans;
    }

    @Override
    public boolean equals(Object p) {
        if (p != null && p instanceof Index2D) {
            boolean ans = true;
            if (this.x == ((Index2D) p).x && this.y == ((Index2D) p).y) {
                ans = true;
                return ans;
            }
            ans = false;
            return ans;
        }
        return false;
    }
}
