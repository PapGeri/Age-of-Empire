package View;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class BaseWindow extends JFrame {

    int mapNum = 1;

    public BaseWindow() {
        setTitle("Not Age of Empires II");

        Image image = new ImageIcon("data/images/icon.png").getImage();
        setIconImage(image);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JButton help = new JButton("Help");
        help.setContentAreaFilled(false);
        help.setBorderPainted(false);

        help.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String out;
                    try (BufferedReader br = new BufferedReader(new FileReader("data/help.txt"))) {
                        String line;
                        out = "";
                        while ((line = br.readLine()) != null) {
                            out += "\n";
                            out += line;
                        }
                    }
                    JOptionPane.showMessageDialog(null, out, "Help", JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        menu.addSeparator();
        JMenu menuSetMap = new JMenu("Choose Level");

        JMenuItem menuLevel1 = new JMenuItem(new AbstractAction("Level 1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMapnum(1);
                JOptionPane.showMessageDialog(null, "Map " + getMapnum() + " has been loaded");
            }
        });

        JMenuItem menuLevel2 = new JMenuItem(new AbstractAction("Level 2") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMapnum(2);
                JOptionPane.showMessageDialog(null, "Map " + getMapnum() + " has been loaded");
            }
        });

        JMenuItem menuExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExitConfirmation();
            }
        });

        menu.add(menuSetMap);
        menuSetMap.add(menuLevel1);
        menuSetMap.add(menuLevel2);
        menu.add(menuExit);

        menuBar.add(menu);
        menuBar.add(help);
        setJMenuBar(menuBar);

        // pack();
        setVisible(true);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }

        });

    }

    private void showExitConfirmation() {
        int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            doUponExit();
        }
    }

    protected void doUponExit() {
        this.dispose();
    }

    protected void setMapnum(int newmapnum) {
        mapNum = newmapnum;
    }

    protected int getMapnum() {
        return mapNum;
    }
}
