import javax.swing.*;
import java.awt.*;
/**
 * Created by Mole on 1/31/2018.
 */
public class SelectionPanel extends JPanel {

    private int width;
    private int height;
    private Dimension size;
    private Unit[] army;

    public SelectionPanel(int w, int h, Unit[] a) {
        width = w;
        height = h;
        size = new Dimension(0, 0);
        army = a;
        //this.setLayout(new GridLayout(8,5));
        this.setLayout(null);
        //this.setOpaque(true);
        //this.setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g){
        int k = 0;
        for(int i = 0, h = 36; i < 8; i++ , h += 116){
            for(int j = 0, w = 30; j < 5; j++ , w += 95){
                //g.setColor(Color.black);
               // g.fillRect(w,h,60, 80);
                JLabel img = new JLabel();
                img.setIcon(army[k].getImage());
               //JLabel img = new JLabel("hi", JLabel.CENTER);//
                img.setLocation(new Point(w,h));
                img.setSize(60,80);
                this.add(img);
                k++;
            }
        }
    }

    public Dimension getPreferredSize(){
        return new Dimension(width,height);
    }
}