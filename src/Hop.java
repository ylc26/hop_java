import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Hop {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;
    public static final int DELAY = 35;

    private JFrame frame;
    private Field field;
    private Axel axel;
    private Timer timer;
    private GamePanel gamePanel;
    private int score;
    private JLabel scoreLabel;

    private int level;
    private final int[] LEVEL_SCORES = {0, 80, 800, 2000, 3200, 4800, 7200};

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private MenuHop menuHopPanel;
    private ScoreHop scoreHop;

    public Hop() {
        frame = new JFrame("Hop!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        field = new Field(WIDTH, HEIGHT, this);
        axel = new Axel(field, WIDTH / 2, Field.START_ALTITUDE, this);
        gamePanel = new GamePanel(field, axel);

        score = 0;
        level = 0;
        scoreLabel = new JLabel("Niveau: 0 | Score: 0  | Sauts disponibles: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);

        menuHopPanel = new MenuHop(cardLayout, cardPanel, this);
        cardPanel.add(menuHopPanel, "Menu");
        scoreHop = new ScoreHop(cardLayout, cardPanel);
        cardPanel.add(scoreHop, "Scores");

        JPanel gameContainer = new JPanel(new BorderLayout());
        gameContainer.add(scoreLabel, BorderLayout.NORTH);
        gameContainer.add(gamePanel, BorderLayout.CENTER);
        cardPanel.add(gameContainer, "Game");

        frame.add(cardPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void round() {
        axel.update();
        field.update();
        field.checkCoinCollision(axel);
        frame.repaint();
    }

    public static void main(String[] args) {
        Hop game = new Hop();

        game.timer = new Timer(DELAY, (ActionEvent e) -> {
            game.round();
            if (game.over()) {
                ScoreHistorique.addScore(game.score);
                game.timer.stop();
                game.gotoMenu();
            }
        });
    }

    public void gotoMenu() {
        SongHop.stopBackground();
        cardLayout.show(cardPanel, "Menu");
    }

    public void startGame() {
        score = 0;
        level = 0;
        field = new Field(WIDTH, HEIGHT, this);
        axel = new Axel(field, WIDTH / 2, Field.START_ALTITUDE, this);
        gamePanel = new GamePanel(field, axel);

        JPanel gameContainer = new JPanel(new BorderLayout());
        gameContainer.add(scoreLabel, BorderLayout.NORTH);
        gameContainer.add(gamePanel, BorderLayout.CENTER);

        cardPanel.add(gameContainer, "Game");
        cardLayout.show(cardPanel, "Game");

        SongHop.playLoop("res/son_hop.wav");

        timer.start();
        gamePanel.requestFocusInWindow();
    }

    public boolean over() {
        return !axel.isSurviving();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
        checkLevelUp();
        updateScoreLabel();
    }

    void updateScoreLabel() {
        scoreLabel.setText("Niveau: " + level + " | Score: " + score + " | Sauts disponibles: " + axel.getExtraJumps());
    }

    public void checkLevelUp() {
        if (level < LEVEL_SCORES.length - 1 && score >= LEVEL_SCORES[level + 1]) {
            level++;
            updateScoreLabel();
            field.increaseDifficulty(level);
        }
    }
}
