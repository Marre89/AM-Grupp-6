import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 *
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
	private Image birbImg;
	private Image backGround;

	public GameSurface(final int width, final int height) {
            this.resetGame();
            this.backGround = Toolkit.getDefaultToolkit().getImage("images/windows.jpg");
            this.birbImg = Toolkit.getDefaultToolkit().getImage("images/birb.png");
        }
	
    public void resetGame() {
        this.gameOver = false;
        this.pillars = new ArrayList<>();
        this.birb = new Rectangle(200, 400, 50, 50);
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
        if(gameOver) {    
            g.setColor(Color.blue);
			g.fillRect(0, 0, d.width, d.height);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("Score: " + score,(d.width / 2) - 110, (d.height / 2) + 70); 
			g.drawString("Highscore: " , (d.width / 2) - 110, (d.height / 2) + 90); 
			g.drawString("Game Over!", d.width / 2 - 110, d.height / 2 - 20);
            resetButton();   
        }
    }

    private void resetButton() {
        final Dimension d = this.getSize();
        JButton restartButton = new JButton("Restart");
		restartButton.setText("Restart");
		restartButton.setSize(120, 40);
		restartButton.setLocation(d.width / 2 - 100, d.height / 2);
        this.add(restartButton);
        restartButton.addActionListener(e -> {
            resetGame();
            this.repaint();
        });
    }
    
    private void paintPillars(Graphics g) {
        for (Rectangle pillar : pillars) {
            g.setColor(Color.blue);
            g.fillRect(pillar.x, pillar.y, pillar.width, pillar.height);
            g.drawString("Score: " + score, 0, WIDTH * WIDTH + 20);
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
			timer.stop();
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
        if(kc ==KeyEvent.VK_R) {
            resetGame();
        }
	}
}

	