import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {

    private static final int BLOCK_HEIGHT = 10;
    public static final int AXEL_WIDTH = 25;
    public static final int AXEL_HEIGHT = 25;

    private final Axel axel;
    private final Field field;
    private Image luigiImage;
    private Image backgroundImage;

    private static final Color BROWN = new Color(139, 69, 19);

    public GamePanel(Field field, Axel axel) {
        this.field = field;
        this.axel = axel;
        setPreferredSize(new Dimension(field.width, field.height));
        addKeyListener(this);
        setFocusable(true);

        try {
            luigiImage = javax.imageio.ImageIO.read(new java.io.File("res/luigi25.png"));
            backgroundImage = javax.imageio.ImageIO.read(new java.io.File("res/mario.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        GradientPaint gradient = new GradientPaint(
                0, field.height - field.getLavaAltitude(), new Color(255, 0, 0, 150),
                0, field.height, new Color(255, 165, 0, 150)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, field.height - field.getLavaAltitude(), field.width, field.getLavaAltitude());

        for (Block block : field.getBlocks()) {
            int blockX = block.getX();
            int blockY = field.height - (block.getY() - field.getLavaAltitude());
            int blockWidth = block.getWidth();
            int blockHeight = BLOCK_HEIGHT;

            if (block instanceof BlockTrampoline) {
                g2d.setColor(Color.BLUE);
            } else if (block instanceof BlockMS) {
                g2d.setColor(Color.GREEN);
            } else if (block instanceof BlockOscillation) {
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(BROWN);
            }

            g2d.fillRect(blockX, blockY, blockWidth, blockHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(blockX, blockY, blockWidth, blockHeight);
        }

        g2d.setColor(Color.YELLOW);
        for (Piece piece : field.getCoins()) {
            int coinSize = 8;
            int coinX = piece.getX() - coinSize / 2;
            int coinY = field.height - (piece.getY() - field.getLavaAltitude()) - coinSize / 2;
            g2d.fillOval(coinX, coinY, coinSize, coinSize);
        }

        int axelX = axel.getX();
        int axelY = field.height - (axel.getY() - field.getLavaAltitude());

        if (luigiImage != null) {
            int imageHeight = luigiImage.getHeight(null);
            g2d.drawImage(luigiImage, axelX, axelY - imageHeight, AXEL_WIDTH, AXEL_HEIGHT, null);
        } else {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(axelX, axelY - AXEL_HEIGHT, AXEL_WIDTH, AXEL_HEIGHT);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                axel.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                axel.setRight(true);
                break;
            case KeyEvent.VK_UP:
                if (!axel.isFalling()) {
                    axel.setJumping(true);
                }
                break;
            case KeyEvent.VK_DOWN:
                axel.setDiving(true);
                break;
            case KeyEvent.VK_SPACE:
                axel.performExtraJump();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                axel.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                axel.setRight(false);
                break;
            case KeyEvent.VK_UP:
                axel.setJumping(false);
                break;
            case KeyEvent.VK_DOWN:
                axel.setDiving(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
