public class BlockOscillation extends Block {
    private int initialY;
    private int amplitude = 20;
    private int direction = 1;

    public BlockOscillation(int x, int y, int width) {
        super(x, y, width);
        this.initialY = y;
    }

    public void updatePosition() {
        int newY = getY() + direction;
        if (newY > initialY + amplitude) {
            newY = initialY + amplitude;
            direction = -1;
        } else if (newY < initialY - amplitude) {
            newY = initialY - amplitude;
            direction = 1;
        }
        setY(newY);
    }
}
