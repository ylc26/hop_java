public class Axel {
    public static final double LATERAL_SPEED = 8;
    public static final double JUMP_SPEED = 20;
    public static final double GRAVITY = 1;
    public static final double DIVE_SPEED = 3 * GRAVITY;
    public static final double MAX_FALL_SPEED = -20;

    private int x, y;
    private double dx, dy;

    private boolean falling;
    private boolean jumping;
    private boolean diving;
    private boolean left;
    private boolean right;

    private boolean surviving;
    private boolean onTrampoline = false;

    private int extraJumps = 0;

    private final Field field;
    private Hop game;

    public Axel(Field f, int x, int y, Hop game) {
        this.field = f;
        this.x = x;
        this.y = y;
        this.game = game;
        this.surviving = true;
        this.falling = false;
        this.dx = 0;
        this.dy = 0;
    }

    public void computeMove() {
        dx = 0;
        if (left) {
            dx = -LATERAL_SPEED;
        }
        if (right) {
            dx = LATERAL_SPEED;
        }

        if (jumping && !falling) {
            if (onTrampoline) {
                dy = JUMP_SPEED * 1.5;
            } else {
                dy = JUMP_SPEED;
            }
            falling = true;
            jumping = false;
        }

        dy -= GRAVITY;
        if (diving) {
            dy -= DIVE_SPEED;
        }
        if (dy < MAX_FALL_SPEED) {
            dy = MAX_FALL_SPEED;
        }
    }

    public void update() {
        computeMove();
        x += dx;
        if (x < 0) {
            x = 0;
        }
        if (x > field.width - GamePanel.AXEL_WIDTH) {
            x = field.width - GamePanel.AXEL_WIDTH;
        }
        checkCollision();
        if (y < field.getLavaAltitude()) {
            surviving = false;
        }
    }

    public void checkCollision() {

        double newY = y + dy;

        if (dy < 0) {
            Block block = field.getCollidingBlock(x, y, (int) newY);
            if (block != null) {
                y = block.getY();
                dy = 0;
                falling = false;

                if (block instanceof BlockMS) {
                    ((BlockMS) block).onLanding();
                }

                onTrampoline = (block instanceof BlockTrampoline);

                if (block.getY() > game.getScore()) {
                    game.setScore(block.getY());
                }
            } else {
                y = (int) newY;
                falling = true;
                onTrampoline = false;
            }
        } else if (dy > 0) {
            y = (int) newY;
            onTrampoline = false;
        } else {
            if (!isStandingOnBlock()) {
                falling = true;
                onTrampoline = false;
            } else {
                Block blockBelow = field.getBlockBelow(x, y);
                onTrampoline = (blockBelow instanceof BlockTrampoline);
                falling = false;
            }
        }
    }

    private boolean isStandingOnBlock() {
        Block blockBelow = field.getBlockBelow(x, y);
        return blockBelow != null && y == blockBelow.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setDiving(boolean diving) {
        this.diving = diving;
    }

    public boolean isSurviving() {
        return surviving;
    }

    public void addExtraJump() {
        extraJumps++;
    }

    public void performExtraJump() {
        if (extraJumps > 0 && falling) {
            dy = JUMP_SPEED;
            extraJumps--;
            falling = true;
            game.updateScoreLabel();
        }
    }

    public int getExtraJumps() {
        return extraJumps;
    }

    public boolean isFalling() {
        return falling;
    }
}
