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
import java.awt.Image;
import java.awt.Toolkit;
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
    private Rectangle spaceShip;
    private int score;
    private int highScore;


    public GameSurface(final int width, final int height) {
        this.gameOver = false;
        this.pillars = new ArrayList<>();
        this.spaceShip = new Rectangle(200, width / 2 - 15, 50, 50);
        this.timer = new Timer(16, this);
        this.tick = 0;
        this.highScore = 0;
        addPillar(width, height);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint(g);
        
    }

    private void addPillar(final int width, final int height) {
        int y = 300;
        int x = 800;
        pillars.add(new Rectangle(x, y, 100, 500));
    }

    private void repaint(Graphics g) {
        final Dimension d = this.getSize();
        
        if (gameOver) {
            // if the current score is greater than high score then high score equals score
            if (score > highScore) {
                highScore = score;
            }
            g.setColor(Color.blue);
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Score: " + score, 0, WIDTH * WIDTH + 20); //
            g.drawString("Highscore: " + highScore, 0, WIDTH * WIDTH + 40); //
            g.drawString("Game Over!", d.width / 2 - 150, d.height / 2);
            return;
        }

        Image img2 = Toolkit.getDefaultToolkit().getImage("images/windows.jpg");
        g.drawImage(img2, 0, 0, d.width, d.height, this);
        g.getClipBounds();

        for (Rectangle pillar : pillars) {
            g.setColor(Color.blue);
            g.fillRect(pillar.x, pillar.y, pillar.width, pillar.height);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(String.valueOf(score), 700, 50);
        }

        // draw the space ship
        Image img1 = Toolkit.getDefaultToolkit().getImage("images/birb.png");
        g.drawImage(img1, spaceShip.x, spaceShip.y, spaceShip.width, spaceShip.height, this);
        spaceShip.translate(0, 2);

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
                score += 10;
                toRemove.add(pillar);
            }

            if (pillar.intersects(spaceShip)) {
                gameOver = true;
            }
            
        }

        pillars.removeAll(toRemove);

        if(++tick % 120 == 0) {
            addPillar(d.width, d.height);
        }
       
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
        final int maxHeight = this.getSize().height - spaceShip.height - 10;
        final int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_SPACE && spaceShip.y > minHeight && spaceShip.y < maxHeight) {
            spaceShip.translate(0, -100);
        }
        if (kc == KeyEvent.VK_SPACE) {
            this.timer.start();
        }
    }
}