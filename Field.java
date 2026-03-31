import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Field {

    public static final int ALTITUDE_GAP = 80;
    public static final int START_ALTITUDE = 40;

    public final int width, height;
    private int bottom, top;
    private int lavaAltitude;

    private List<Block> blocks;
    private List<Piece> pieces;

    private int lavaRiseSpeed;
    private int minBlockWidth;
    private int maxBlockWidth;

    private final int[] LAVA_RISE_SPEEDS = {0, 1, 2, 3, 4, 5, 6};
    private final int[] MIN_BLOCK_WIDTHS = {50, 45, 40, 35, 30, 25, 20};
    private final int[] MAX_BLOCK_WIDTHS = {100, 90, 80, 70, 60, 50, 40};

    private int nextBlockAltitude = START_ALTITUDE;

    private Hop game;

    private Axel axel;
    private int level;

    public Field(int width, int height, Hop game) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.blocks = new ArrayList<>();
        this.pieces = new ArrayList<>();
        increaseDifficulty(0);
        this.lavaAltitude = 0;
        this.bottom = lavaAltitude;
        this.top = bottom + height;
        initBlocks();
    }

    public int getLavaAltitude() {
        return lavaAltitude;
    }

    public Block getBlockBelow(int x, int y) {
        for (Block block : blocks) {
            if (block.getY() == y && x + GamePanel.AXEL_WIDTH > block.getX() && x < block.getX() + block.getWidth()) {
                return block;
            }
        }
        return null;
    }

    private void generateNewBlocks() {
        while (top >= nextBlockAltitude) {
            int blockWidth = getRandomInt(minBlockWidth, maxBlockWidth);
            int xPosition = getRandomInt(0, width - blockWidth);

            double rand = Math.random();
            Block block;
            if (rand < 0.1) {
                block = new BlockTrampoline(xPosition, nextBlockAltitude, blockWidth);
            } else if (rand < 0.2) {
                block = new BlockMS(xPosition, nextBlockAltitude, blockWidth);
            } else if (rand < 0.3) {
                block = new BlockOscillation(xPosition, nextBlockAltitude, blockWidth);
            } else {
                block = new Block(xPosition, nextBlockAltitude, blockWidth);
            }

            blocks.add(block);
            nextBlockAltitude += ALTITUDE_GAP;
            generateCoins(block);
        }
    }

    private void generateCoins(Block block) {
        if (Math.random() < 0.4) {
            int coinX = block.getX() + block.getWidth() / 2;
            int coinY = block.getY() + 20;
            pieces.add(new Piece(coinX, coinY));
        }
    }

    public void increaseDifficulty(int level) {
        int index = Math.min(level, LAVA_RISE_SPEEDS.length - 1);
        lavaRiseSpeed = LAVA_RISE_SPEEDS[index];
        minBlockWidth = MIN_BLOCK_WIDTHS[index];
        maxBlockWidth = MAX_BLOCK_WIDTHS[index];
    }

    private void removeOldBlocks() {
        blocks.removeIf(block -> block.getY() < bottom);
        pieces.removeIf(piece -> piece.getY() < bottom);
    }

    public void update() {
        lavaAltitude += lavaRiseSpeed;
        bottom = lavaAltitude;
        top = bottom + height;
        generateNewBlocks();
        removeOldBlocks();
        blocks.removeIf(block -> block instanceof BlockMS && ((BlockMS) block).shouldDisappear());

        for (Block block : blocks) {
            if (block instanceof BlockOscillation) {
                ((BlockOscillation) block).updatePosition();
            }
        }
    }

    private void initBlocks() {
        int initialBlockWidth = 100;
        int axelStartX = width / 2;
        int initialBlockX = axelStartX - initialBlockWidth / 2;
        if (initialBlockX < 0) {
            initialBlockX = 0;
        }
        if (initialBlockX + initialBlockWidth > width) {
            initialBlockX = width - initialBlockWidth;
        }

        Block initialBlock = new Block(initialBlockX, START_ALTITUDE, initialBlockWidth);
        blocks.add(initialBlock);

        int axelStartY = initialBlock.getY();
        axel = new Axel(this, axelStartX, axelStartY, game);

        int currentAltitude = START_ALTITUDE + ALTITUDE_GAP;
        int maxAltitude = 1000;
        while (currentAltitude <= maxAltitude) {
            int blockWidth = getRandomInt(50, 150);
            int xPosition = getRandomInt(0, width - blockWidth);
            Block block = new Block(xPosition, currentAltitude, blockWidth);
            blocks.add(block);
            currentAltitude += ALTITUDE_GAP;
        }
    }

    private int getRandomInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Piece> getCoins() {
        return pieces;
    }

    public void checkCoinCollision(Axel axel) {
        for (Iterator<Piece> it = pieces.iterator(); it.hasNext();) {
            Piece piece = it.next();
            if (Math.abs(axel.getX() + GamePanel.AXEL_WIDTH / 2 - piece.getX()) < GamePanel.AXEL_WIDTH / 2 &&
                    Math.abs(axel.getY() - piece.getY()) < GamePanel.AXEL_HEIGHT / 2) {
                it.remove();
                axel.addExtraJump();
                game.setScore(game.getScore() + 100);
            }
        }
    }

    public Block getCollidingBlock(int x, int yOld, int yNew) {
        for (Block block : blocks) {
            if (yNew <= block.getY() && block.getY() <= yOld) {
                if (x + GamePanel.AXEL_WIDTH > block.getX() && x < block.getX() + block.getWidth()) {
                    return block;
                }
            }
        }
        return null;
    }

}
