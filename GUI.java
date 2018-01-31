import javax.swing.*;
import java.awt.*;

/**
 * Created by Mole on 1/24/2018.
 */
public class GUI {

    private static JFrame frame;
    private static Container contentPane;
    private static Map map;

    public GUI() {
        makeFrame();
    }

    public void makeFrame(){
        frame = new JFrame("Stratego AI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem quit = new JMenuItem("Quit");
        file.add(quit);
        menu.add(file);
        frame.setJMenuBar(menu);

        quit.addActionListener(e -> exit());

        contentPane.setLayout((new BorderLayout()));
        map = new Map(600,800);

        contentPane.add(map, BorderLayout.CENTER);

        frame.setPreferredSize(new Dimension(1000, 900));
        frame.pack();
        frame.setVisible(true);
    }

    private void exit(){
        System.exit(1);
    }
}
