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
 * A simple panel with a space invaders "game" in it. This is just to
 * demonstrate the bare minimum of stuff than can be done drawing on a panel.
 * This is by no means very good code.
 * 
 */
public class GameSurface extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 6260582674762246325L;

    private boolean gameOver;
    private Timer timer;
    private List<Rectangle> pillars;
    private Rectangle spaceShip;
    private int score;


    public GameSurface(final int width, final int height) {
        this.gameOver = false;
        this.pillars = new ArrayList<>();

        addPillar(width, height);
    
        this.spaceShip = new Rectangle(200, width / 2 - 15, 50, 50);
        this.timer = new Timer(1, this);
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

    /*
     * private void gravity(Rectangle spaceShip) { int y = pos.nextInt(); }
     */
    /**
     * Call this method when the graphics needs to be repainted on the graphics
     * surface.
     * 
     * @param g the graphics to paint on
     */
    private void repaint(Graphics g) {
        final Dimension d = this.getSize();
        
        if (gameOver) {
            g.setColor(Color.blue);
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Test Test!!", d.width / 2 - 150, d.height / 2);
            return;
        }

        Image img2 = Toolkit.getDefaultToolkit().getImage("windows.jpg");
        g.drawImage(img2, 0, 0, d.width, d.height, this);
        g.getClipBounds();

        for (Rectangle pillar : pillars) {
            /*
             * FontMetrics metrics = g.getFontMetrics(getFont()); int x = alien.x +
             * (alien.width - metrics.stringWidth(text)) /2-24; int y = alien.y +
             * ((alien.height - metrics.getHeight()) / 2) + metrics.getAscent();
             */
            g.setColor(Color.blue);
            g.fillRect(pillar.x, pillar.y, pillar.width, pillar.height);

            /*
             * g.setColor(Color.white); g.setFont(new Font("Arial", Font.BOLD, 20));
             * g.drawString(text, x, y);
             */
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(String.valueOf(score), 700, 50);
        }

        // draw the space ship
        Image img1 = Toolkit.getDefaultToolkit().getImage("birb.png");
        g.drawImage(img1, spaceShip.x, spaceShip.y, spaceShip.width, spaceShip.height, this);
        spaceShip.translate(0, 2);

        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this will trigger on the timer event
        // if the game is not over yet it will
        // update the positions of all aliens
        // and check for collision with the space ship

        if (gameOver) {
            timer.stop();
            return;
        }

        final List<Rectangle> toRemove = new ArrayList<>();

        for (Rectangle pillar : pillars) {
            pillar.translate(-3, 0);
            if (pillar.x + pillar.width < 0) {
                score += 10;
                toRemove.add(pillar);
            }

            

            if (pillar.intersects(spaceShip)) {
                gameOver = true;
            }
            
        }

        pillars.removeAll(toRemove);

         // final Rectangle plane = new Rectangle(getBounds());

        // add new aliens for every one that was removed
        for (int i = 0; i < toRemove.size(); ++i) {
            Dimension d = getSize();
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