package assignments.Ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     *
     */
    private int[][] _map_3_3 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
    private Map2D _m0, _m1, _m3_3;

    @BeforeEach
    public void setuo() {
        _m0 = new Map(1, 1, 0);
        _m1 = new Map(1, 1, 0);
        _m3_3 = new Map(_map_3_3);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int[500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3, 2);
        _m1.fill(p1, 1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }

    @Test
    void testEquals() {
        assertEquals(_m0, _m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }
    @Test
    void testAddMap2D() {
        Map m1 = new Map(3, 2, 0);
        Map m2 = new Map(3, 2, 0);
        m1.setPixel(0, 0, 1);
        m1.setPixel(1, 0, 2);
        m1.setPixel(0, 1, 3);
        m2.setPixel(0, 0, 10);
        m2.setPixel(1, 0, 20);
        m2.setPixel(0, 1, 30);
        m1.addMap2D(m2);
        assertEquals(11, m1.getPixel(0, 0));
        assertEquals(22, m1.getPixel(1, 0));
        assertEquals(33, m1.getPixel(0, 1));
        assertEquals(10, m2.getPixel(0, 0));
        assertEquals(20, m2.getPixel(1, 0));
        assertEquals(30, m2.getPixel(0, 1));
    }
    @Test
    void testRescaleExampleFromDoc() {
        Map m = new Map(100, 200, 0);
        m.rescale(1.2, 0.5);
        assertEquals(120, m.getWidth());
        assertEquals(100, m.getHeight());
    }
    @Test
    void testGetSetPixelByPoint() {
        Map2D m = new Map(3, 2, 0);
        Pixel2D p = new Index2D(1, 0);
        m.setPixel(p, 5);
        assertEquals(5, m.getPixel(p));
        assertEquals(5, m.getPixel(1, 0));
    }
    @Test
    void testSameDimensions() {
        Map m1 = new Map(4, 3, 0);
        Map m2 = new Map(4, 3, 1);
        Map m3 = new Map(3, 4, 0);
        assertTrue(m1.sameDimensions(m2));
        assertFalse(m1.sameDimensions(m3));
    }
    @Test
    void testDrawLineSimple() {
        Map m = new Map(5, 5, 0);
        Pixel2D p1 = new Index2D(0, 0);
        Pixel2D p2 = new Index2D(4, 2);
        m.drawLine(p1, p2, 7);
        assertEquals(7, m.getPixel(0, 0));
        assertEquals(7, m.getPixel(1, 1));
        assertEquals(7, m.getPixel(2, 1));
        assertEquals(7, m.getPixel(3, 2));
        assertEquals(7, m.getPixel(4, 2));
        assertEquals(0, m.getPixel(4, 4));
    }
    @Test
    void testDrawRectBasic() {
        Map m = new Map(5, 4, 0);
        Pixel2D p1 = new Index2D(1, 1);
        Pixel2D p2 = new Index2D(3, 2);
        m.drawRect(p1, p2, 7);
        assertEquals(7, m.getPixel(1, 1));
        assertEquals(7, m.getPixel(2, 1));
        assertEquals(7, m.getPixel(3, 1));
        assertEquals(7, m.getPixel(1, 2));
        assertEquals(7, m.getPixel(2, 2));
        assertEquals(7, m.getPixel(3, 2));
        assertEquals(0, m.getPixel(0, 0));
        assertEquals(0, m.getPixel(4, 3));
    }
    @Test
    void testDrawCircleBasic() {
        Map m = new Map(5, 5, 0);
        Pixel2D center = new Index2D(2, 2);
        m.drawCircle(center, 1.5, 7);
        assertEquals(7, m.getPixel(2, 2));
        assertEquals(7, m.getPixel(1, 2));
        assertEquals(7, m.getPixel(2, 1));
        assertEquals(7, m.getPixel(3, 2));
        assertEquals(7, m.getPixel(2, 3));
        assertEquals(0, m.getPixel(0, 2));
    }
    @Test
    void testMulScalar() {
        Map m = new Map(3, 2, 0);
        m.setPixel(0, 0, 2);
        m.setPixel(1, 0, 3);
        m.setPixel(0, 1, -4);
        m.mul(1.5);
        assertEquals(3,  m.getPixel(0, 0));
        assertEquals(4,  m.getPixel(1, 0));
        assertEquals(-6, m.getPixel(0, 1));
    }
    @Test
    void testGetSetPixelByXY() {
        Map2D m = new Map(3, 2, 0);
        m.setPixel(2, 1, 9);
        assertEquals(9, m.getPixel(2, 1));
        assertEquals(0, m.getPixel(0, 0));
        assertEquals(0, m.getPixel(1, 1));
    }
    @Test
    void testSetPixelXY() {
        Map m = new Map(3, 3, 0);
        m.setPixel(1, 2, 9);
        assertEquals(9, m.getPixel(1, 2));
        assertEquals(0, m.getPixel(0, 0));
    }
    @Test
    void testIsInside() {
        Map2D m = new Map(4, 3, 0);
        assertTrue(m.isInside(new Index2D(0, 0)));
        assertTrue(m.isInside(new Index2D(3, 2)));
        assertFalse(m.isInside(new Index2D(-1, 0)));
        assertFalse(m.isInside(new Index2D(0, -1)));
        assertFalse(m.isInside(new Index2D(4, 0)));
        assertFalse(m.isInside(new Index2D(0, 3)));
    }
    @Test
    public void testAllDistance() {
        Map m = new Map(3, 3, 0);
        Index2D start = new Index2D(0, 0);
        Map2D dist = m.allDistance(start, -1, false);
        assertEquals(0, dist.getPixel(new Index2D(0, 0)));
        assertEquals(1, dist.getPixel(new Index2D(1, 0)));
        assertEquals(1, dist.getPixel(new Index2D(0, 1)));
        assertEquals(2, dist.getPixel(new Index2D(1, 1)));
        assertEquals(4, dist.getPixel(new Index2D(2, 2)));
    }
}