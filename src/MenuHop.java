import javax.swing.*;
import java.awt.*;

public class MenuHop extends JPanel {
    private Image backgroundImage;

    private JButton playButton;
    private JButton scoresButton;
    private JButton quitButton;

    private TitleLabel titleLabel;

    public MenuHop(CardLayout cl, JPanel cardPanel, Hop game) {
        setPreferredSize(new Dimension(Hop.WIDTH, Hop.HEIGHT));

        try {
            backgroundImage = javax.imageio.ImageIO.read(new java.io.File("res/volcan1.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        titleLabel = new TitleLabel("HOP !");
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        playButton = createVolcanButton("Jouer");
        scoresButton = createVolcanButton("Scores");
        quitButton = createVolcanButton("Quitter");

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(playButton);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(scoresButton);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(quitButton);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        playButton.addActionListener(e -> game.startGame());
        scoresButton.addActionListener(e -> cl.show(cardPanel, "Scores"));
        quitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createVolcanButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(new Color(255, 200, 0));
        btn.setBackground(new Color(60, 20, 0));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 80, 0), 3));
        btn.setOpaque(true);
        btn.setMargin(new Insets(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Image scaled = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(scaled, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private static class TitleLabel extends JComponent {
        private String text;

        public TitleLabel(String text) {
            this.text = text;
            setPreferredSize(new Dimension(Hop.WIDTH, 70));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("Arial Black", Font.BOLD, 70);
            g2d.setFont(font);

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - 10;

            GradientPaint grad = new GradientPaint(x, y - textHeight, new Color(255, 140, 0),
                    x, y, new Color(255, 0, 0));
            g2d.setPaint(grad);
            g2d.drawString(text, x, y);
        }
    }
}
