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
        this.setLayout(new GridLayout(10,4));
        //this.setOpaque(true);
        //this.setBackground(Color.WHITE);
        for(int i = 0; i < army.length; i++){
            //g.setColor(Color.black);
            // g.fillRect(w,h,60, 80);
            JLabel img;
            //img = new JLabel(army[i].getImage());
            //img = new JLabel("hi");//
            //this.add(img);
        }
    }

    @Override
    public void paintComponent(Graphics g){

    }

    public Dimension getPreferredSize(){
        return new Dimension(width,height);
    }
}