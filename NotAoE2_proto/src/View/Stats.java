package View;

import java.awt.*;
import javax.swing.*;
public class Stats extends JFrame{
    
    public Stats(){
        setTitle("Stats");
        
        Image image = new ImageIcon("data/images/icon.png").getImage();
        setIconImage(image);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(1350,115);
        setFocusableWindowState(false);
        //setLocation(370,115);
        setSize(200,300);
        setResizable(false);
        setVisible(true);
    } 
}
