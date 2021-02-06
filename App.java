import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import java.awt.Toolkit;
import java.awt.Dimension;

/**
 * This is a very small "game" just to show the absolute basics of
 * how to draw on a surface in a frame using Swing/AWT.
 * 
 */
public class App {
    private static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static void main(String[] args) {
        JFrame main = new JFrame("Jumpy Birb");

        GameSurface gs = new GameSurface(800, 600);

        main.setSize(800, 600);
        main.setMinimumSize(new Dimension(SCREEN_WIDTH*1/4, SCREEN_HEIGHT*1/4));
        main.setLocationRelativeTo(null);
        main.setResizable(true);
        main.add(gs);
        main.addKeyListener(gs);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);
        main.setVisible(true);
       
    }
}
