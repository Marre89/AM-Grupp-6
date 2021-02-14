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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
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
	private String highScore = "";
	private Image birbImg;
	private Image backGround;
	private JButton restartButton;

	public GameSurface(final int width, final int height) {
		this.gameOver = false;
		this.pillars = new ArrayList<>();
		this.birb = new Rectangle(200, width / 2 - 15, 50, 50);
		this.timer = new Timer(16, this);
		this.tick = 0;
		this.backGround = Toolkit.getDefaultToolkit().getImage("images/windows.jpg");
		this.birbImg = Toolkit.getDefaultToolkit().getImage("images/birb.png");
		addPillar(width, height);
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		repaint(g);
	}

	private void addPillar(final int width, final int height) {
		int random = ThreadLocalRandom.current().nextInt(0, 200);
		pillars.add(new Rectangle(800, 0, 100, 300 - random)); // Upper pillar
		pillars.add(new Rectangle(800, 500 - random, 100, 400)); // Lower pillar
	}

	public void repaint(Graphics g) {
		final Dimension d = this.getSize();

		if (gameOver) {

			if (highScore.equals("")) {
				highScore = this.GetScore();
			}
			
			CheckScore();

			JButton restartButton = new JButton("Restart");
			restartButton.setText("Restart");
			restartButton.setSize(120, 40);
			restartButton.setLocation(d.width / 2 - 100, d.height / 2);
			this.add(restartButton);
			restartButton.addActionListener(this);

			g.setColor(Color.blue);
			g.fillRect(0, 0, d.width, d.height);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("Score: " + score,(d.width / 2) - 110, (d.height / 2) + 70); 
			g.drawString("Highscore: " + highScore, (d.width / 2) - 110, (d.height / 2) + 90); 
			g.drawString("Game Over!", d.width / 2 - 110, d.height / 2 - 20);
			

			return;
		}
		// background
		g.drawImage(backGround, 0, 0, d.width, d.height, this);

		for (Rectangle pillar : pillars) {
			g.setColor(Color.blue);
			g.fillRect(pillar.x, pillar.y, pillar.width, pillar.height);
			g.drawString("Score: " + score, 0, WIDTH * WIDTH + 20);
		}

		// draw the space ship
		g.drawImage(birbImg, birb.x, birb.y, birb.width, birb.height, this);
		birb.translate(0, 4);
	}

	public void CheckScore() {
		
		//if (highScore.equals(""));
		//return;
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		final Dimension d = this.getSize();

		if (gameOver) {
			timer.stop();
			return;
		}

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

		if (++tick % 60 == 0) {
			addPillar(d.width, d.height);
			score += 10;
		}

		if (birb.y + birb.height > d.getHeight()) {
			gameOver = true;
		}

		if (e.getSource() == restartButton) {
			// Need to Add restart function on click

		}

		this.repaint();
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
	}

}