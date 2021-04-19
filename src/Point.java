/*
 * A class to hold a pair of x and y coordinates as integers.
 */

public class Point {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
     * Return the X value.
     */
    public int getX() {
        return this.x;
    }

    /*
     * Return the Y value.
     */
    public int getY() {
        return this.y;
    }
}
