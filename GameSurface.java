import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Game Surface class is the class for rendering graphics and game logic.
 * 
 * @param tick            is used to set a specific interval which constrols
 *                        pillar spawn rate.
 * @param List<Rectangle> used for storing pillars.
 */
public class GameSurface extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 6260582674762246325L;

    private boolean gameOver;
    private Timer timer;
    private long tick;
    private List<Rectangle> pillars;
    private Rectangle birb;
    private int score = 0;
    private String highScore = "";
    private Image birbImg;
    private Image backGround;
    private Image pipe;

    /**
     * @param width  of the Game Surface
     * @param height of the Game Surface
     */
    public GameSurface(final int width, final int height) {
        this.resetGame();
        this.pipe = Toolkit.getDefaultToolkit().getImage("images/pillartop.png");
        this.backGround = Toolkit.getDefaultToolkit().getImage("images/windows.jpg");
        this.birbImg = Toolkit.getDefaultToolkit().getImage("images/birb.png");

    }

    /**
     * Sets the game variables to it's start value this.resetGame()
     */
    public void resetGame() {
        this.gameOver = false;
        this.pillars = new ArrayList<>();
        this.birb = new Rectangle(200, 400, 70, 70);
        this.timer = new Timer(16, this);
        this.tick = 0;
        this.score = 0;
        addPillar();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint(g);
    }

    /**
     * Adds a rectangle to pillar list with a random hole location.
     */

    private void addPillar() {
        int random = ThreadLocalRandom.current().nextInt(0, 200);
        pillars.add(new Rectangle(800, 0, 100, 300 - random)); // Upper pillar
        pillars.add(new Rectangle(800, 500 - random, 100, 400)); // Lower pillar
    }

    /**
     * Draws a Game Over screen when gameOver = true.
     * @param g
     */

    public void gameOverScreen(Graphics g) {
        final Dimension d = this.getSize();
        if (gameOver) {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawImage(birbImg, (d.width / 2) - 80, 250, 150, 150, this);
            g.drawString("Score: " + score, (d.width / 2) - 110, 100);
            g.drawString("Highscore: " + highScore, (d.width / 2) - 110, 150);
            g.drawString("Press ENTER to restart", (d.width / 2) - 110, 500);
            g.drawString("GAME OVER!", (d.width / 2 - 110), 50);
        }
    }

    /**
     * Draws image on to the pillar rectangle.
     */

    private void paintPillars(Graphics g) {
        for (Rectangle pillar : pillars) {
            g.drawImage(pipe, pillar.x, pillar.y, pillar.width, pillar.height, null);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("Score: " + score, 680, 20);
        }
    }

    /**
     * Draws background image.
     */
    private void paintBackground(Graphics g) {
        final Dimension d = this.getSize();
        g.drawImage(backGround, 0, 0, d.width, d.height, this);
    }

    /**
     * Draws birb image on to birb location and controls the birb fall interval.
     * @param g
     */
    private void paintBirb(Graphics g) {
        g.drawImage(birbImg, birb.x, birb.y, birb.width, birb.height, this);
        birb.translate(0, 6);
    }

    /**
     * Paints all the methods on to the screen.
     * @param g
     */
    private void repaint(Graphics g) {
        paintBackground(g);
        paintPillars(g);
        paintBirb(g);
        gameOverScreen(g);
    }

    /**
     * Remove pillars which are no longer visible and set the pillar speed. Checks
     * if birb has intersected any pillar.
     */
    public void removePillars() {
        final List<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle pillar : pillars) {
            pillar.translate(-6, 0);
            if (pillar.x + pillar.width < 0) {
                toRemove.add(pillar);
            }
            if (pillar.intersects(birb)) {
                gameOver = true;
            }
        }
        pillars.removeAll(toRemove);
    }

    public void spawnPillarAndUpdateScore() {
        if (++tick % 60 == 0) {
            addPillar();
            score += 10;
        }
    }

    /**
     * If birb touches ground gameOver = true.
     */
    public void birbtouchesGround() {
        final Dimension d = this.getSize();
        if (birb.y + birb.height > d.getHeight()) {
            gameOver = true;
        }
    }

    /**
     * Stops the timer and calls getScore() and checkScore().
     */
    public void gameOver() {
        if (gameOver) {
            if (highScore.equals("")) {
                highScore = this.getScore();
            }
            timer.stop();
            checkScore();
        }
    }

    /**
     * Checks if the last score is higher than highScore. Creates a highScore file
     * if no file exists also saves the name and score as a String. Input dialog box
     * shows up if a player reached highScore.
     */
    public void checkScore() {

        if (score > Integer.parseInt((highScore.split(":")[1]))) {

            String name = JOptionPane.showInputDialog("What's your name?");
            highScore = name + ":" + score;

            File highScoreFile = new File("score.txt");

            if (!highScoreFile.exists()) {
                try {
                    highScoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter writeFile = null;
            BufferedWriter writer = null;

            try {
                writeFile = new FileWriter(highScoreFile);
                writer = new BufferedWriter(writeFile);
                writer.write(this.highScore);

            } catch (Exception e) {

            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * Reads the highScore file.
     * @return String if no highScore exits.
     */

    public String getScore() {

        FileReader readFile = null;
        BufferedReader reader = null;

        try {
            readFile = new FileReader("score.txt");
            reader = new BufferedReader(readFile);
            return reader.readLine();
        } catch (Exception e) {
            return "Nobody:0";
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameOver();
        removePillars();
        spawnPillarAndUpdateScore();
        birbtouchesGround();
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    /**
     * Start the game and make the birb jump by pressing the spacebar. Start a new
     * game session by pressing Enter which resets the game.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        final int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_SPACE) {
            birb.translate(0, -100);
            this.timer.start();
        }
        if (kc == KeyEvent.VK_ENTER) {
            resetGame();
        }
    }
}