public class BlockMS extends Block {
    private long landedTime = -1;

    public BlockMS(int x, int y, int width) {
        super(x, y, width);
    }

    public void onLanding() {
        if (landedTime == -1) {
            landedTime = System.currentTimeMillis();
        }
    }

    public boolean shouldDisappear() {
        if (landedTime == -1) return false;
        return System.currentTimeMillis() - landedTime > 1000;
    }
}

