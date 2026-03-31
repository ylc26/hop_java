public class Block {
    private int x, y;
    private int width;

    public Block(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getWidth() { return width; }

    protected void setY(int newY) {
        this.y = newY;
    }
}
