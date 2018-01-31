import javax.swing.*;
import java.awt.*;
/**
 * Created by Mole on 1/24/2018.
 */
public class Map extends JPanel{

    private int width;
    private int height;
    private Dimension size;

    public Map(int w, int h){
        width = w;
        height = h;
        size = new Dimension(0,0);
    }

    @Override
    public void paintComponent(Graphics g){
        for(int i = 0; i < width; i += width / 10){
            for(int j = 0; j < height; j += height / 10){
                if(j < 4 * height / 10){
                    g.setColor(Color.red);
                    g.fillRect(i,j,width / 10, height / 10);
                } else if(j >= 6 * height / 10){
                    g.setColor(Color.blue);
                    g.fillRect(i,j,width / 10, height / 10);
                } else if((i >= 2 * width / 10 && i < 4 * width / 10) || (i >= 6 * width / 10 && i < 8 * width / 10)){
                    g.setColor(Color.green);
                    g.fillRect(i,j,width / 10, height / 10);
                }else {
                    g.setColor(Color.gray);
                    g.fillRect(i,j,width / 10, height / 10);
                }
            }
        }
        for(int i = 0; i <= width; i += width / 10) {
            g.setColor(Color.black);
            g.drawLine(i,0,i,height);
        }
        for(int i = 0; i <= height; i += height / 10){
            g.setColor(Color.black);
            g.drawLine(0,i,width,i);
        }
    }
}