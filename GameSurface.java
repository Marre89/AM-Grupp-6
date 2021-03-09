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
 * 
 * More work to be done!
 * 
 * 
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

    public GameSurface(final int width, final int height) {
        this.resetGame();
        this.pipe = Toolkit.getDefaultToolkit().getImage("images/pillartop.png");
        this.backGround = Toolkit.getDefaultToolkit().getImage("images/windows.jpg");
        this.birbImg = Toolkit.getDefaultToolkit().getImage("images/birb.png");

    }

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

    private void addPillar() {
        int random = ThreadLocalRandom.current().nextInt(0, 200);
        pillars.add(new Rectangle(800, 0, 100, 300 - random)); // Upper pillar
        pillars.add(new Rectangle(800, 500 - random, 100, 400)); // Lower pillar
    }

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

    private void paintPillars(Graphics g) {
        for (Rectangle pillar : pillars) {
            g.drawImage(pipe, pillar.x, pillar.y, pillar.width, pillar.height, null);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("Score: " + score, 680, 20);
        }
    }

    private void paintBackground(Graphics g) {
        final Dimension d = this.getSize();
        g.drawImage(backGround, 0, 0, d.width, d.height, this);
    }

    private void paintBirb(Graphics g) {
        g.drawImage(birbImg, birb.x, birb.y, birb.width, birb.height, this);
        birb.translate(0, 6);
    }

    private void repaint(Graphics g) {
        paintBackground(g);
        paintPillars(g);
        paintBirb(g);
        gameOverScreen(g);
    }

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

    public void birbtouchesGround() {
        final Dimension d = this.getSize();
        if (birb.y + birb.height > d.getHeight()) {
            gameOver = true;
        }
    }

    public void gameOver() {
        if (gameOver) {
            if (highScore.equals("")) {
                highScore = this.GetScore();
            }
            timer.stop();
            CheckScore();
        }
    }

    public void CheckScore() {

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

    public String GetScore() {

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
        //
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int minHeight = 10;
        final int maxHeight = this.getSize().height - birb.height - 10;
        final int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_SPACE && birb.y > minHeight && birb.y < maxHeight) {
            birb.translate(0, -100);
        }
        if (kc == KeyEvent.VK_SPACE) {
            this.timer.start();
        }
        if (kc == KeyEvent.VK_ENTER) {
            resetGame();
        }
    }
}