import javax.swing.*;
import java.awt.*;
/**
 * Created by Mole on 1/31/2018.
 */
public class SelectionPanel extends JPanel {

    private int width;
    private int height;
    private Dimension size;

    public SelectionPanel(int w, int h) {
        width = w;
        height = h;
        size = new Dimension(0, 0);
    }

    @Override
    public void paintComponent(Graphics g){
        for(int i = 0, h = 36; i < 8; i++ , h += 116){
            for(int j = 0, w = 30; j < 5; j++ , w += 95){
                g.setColor(Color.yellow);
                g.fillRect(w,h,60, 80);
            }
        }
    }

    public Dimension getPreferredSize(){
        return new Dimension(width,height);
    }
}