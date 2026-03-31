import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScoreHop extends JPanel {
    private JTextArea scoreArea;
    private JButton backButton;

    private Image backgroundImage;
    private TitleLabel titleLabel;

    public ScoreHop(CardLayout cl, JPanel cardPanel) {
        setPreferredSize(new Dimension(Hop.WIDTH, Hop.HEIGHT));
        setLayout(new BorderLayout());

        try {
            backgroundImage = javax.imageio.ImageIO.read(new java.io.File("res/volcan2.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        titleLabel = new TitleLabel("Historique des Scores");
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setOpaque(false);
        scoreArea.setForeground(new Color(255, 200, 0));
        scoreArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(scoreArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        backButton = createVolcanButton("Retour");
        backButton.addActionListener(e -> cl.show(cardPanel, "Menu"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                updateScores();
            }
        });
    }

    private JButton createVolcanButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(new Color(255, 200, 0));
        btn.setBackground(new Color(60, 20, 0));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 80, 0), 2));
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateScores() {
        List<Integer> allScores = ScoreHistorique.getScores();
        scoreArea.setText("");
        scoreArea.append("Meilleur score : " + ScoreHistorique.getBestScore() + "\n\n");
        scoreArea.append("Historique des scores : \n");
        for (int s : allScores) {
            scoreArea.append(s + "\n");
        }
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
            setPreferredSize(new Dimension(Hop.WIDTH, 100));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("Arial Black", Font.BOLD, 24);
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
