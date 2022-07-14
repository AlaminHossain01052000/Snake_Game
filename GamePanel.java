import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 120;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new DimensionUIResource(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
                // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; ++i) {
                //     g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                //     g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                // }
            g.setColor(new Color(238, 82, 83));// Apple Color
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);// Apple Size

            // draw the snake
            for (int i = 0; i < bodyParts; ++i) {
                if (i == 0) {// Draw the head
                    g.setColor(new Color(52, 31, 151));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(95, 39, 205));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                }
            }
            // Display the score in the top
            g.setColor(new Color(254, 202, 87));
            g.setFont(new Font("Bahnschrift", Font.BOLD, 40));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("SCORE : " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE : " + appleEaten)) / 2,g.getFont().getSize());// Cener positioning the GAME OVER Fonts

            
           
        } else {
            gameOver(g);
        }

    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;// position the apply in x axis
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;// position the apply in y axis
    }

    public void move() {
        for (int i = bodyParts; i > 0; --i) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            appleEaten++;
            bodyParts++;
            newApple();
        }
    }

    public void checkCollisions() {
        // if the snake hits its own body
        for (int i = bodyParts; i > 0; --i) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // if the snake hit any border

        // Left border checking
        if (x[0] < 0) {
            running = false;
        }

        // Right border checking
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Top border checking
        if (y[0] < 0) {
            running = false;
        }

        // Bottom border checking
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();

        }
    }

    public void gameOver(Graphics g) {
        // Display the score after game over
        g.setColor(new Color(200, 214, 229));
        g.setFont(new Font("Bahnschrift", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("SCORE : " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE : " + appleEaten)) / 2,
                g.getFont().getSize());// Cener positioning the GAME OVER Fonts

        // Game ver text
        g.setColor(new Color(238, 82, 83));
        g.setFont(new Font("Bahnschrift", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);// Cener
                                                                                                             // positioning
                                                                                                             // the GAME
                                                                                                             // OVER
                                                                                                             // Fonts
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
