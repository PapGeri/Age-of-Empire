package View;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Menu extends BaseWindow {

    private JLabel gameStatLabel;
    private Board board;
    private JPanel panel;

    public Menu() {

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JRadioButton poor = new JRadioButton("POOR");
        JRadioButton average = new JRadioButton("AVERAGE");
        JRadioButton rich = new JRadioButton("RICH");

        JButton start = new JButton("Let's Battle!");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ButtonGroup group = new ButtonGroup();
        group.add(poor);
        group.add(average);
        group.add(rich);

        panel.add(poor);
        panel.add(average);
        panel.add(rich);
        panel.add(start, gbc);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (poor.isSelected()) {
                    board = new Board(100, getMapnum());
                }
                if (average.isSelected()) {
                    board = new Board(250, getMapnum());
                }
                if (rich.isSelected()) {
                    board = new Board(500, getMapnum());
                }
            }
        });

        add(panel);
        setVisible(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    @Override
    protected void doUponExit() {
        System.exit(0);
    }
}
