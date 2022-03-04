package me.longbow122.Homeroom;

import com.sun.glass.ui.Window;
import me.longbow122.Homeroom.utils.GUIUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main class which will store all called programs, and the main method to be run.
 */
public class Main {

    /*
    Main method to be called.
    The called program
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JFrame frame = new GUIUtils().getMainHomeroomFrame("Homeroom", 1500, 1500);
        frame.setVisible(true);
        JButton button = new GUIUtils().addButtonToFrame(frame, "Clicky Clicky", 100, 100, 400, 400);
        JLabel label = new GUIUtils().addLabelToFrame(frame, "Testing UwU", 410, 340, 100, 100);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "L bozo", "kekchamp", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
