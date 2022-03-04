package me.longbow122.Homeroom.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for the core GUI work that needs to be done within Homeroom.
 */
public class GUIUtils {

    /**
     * Will produce an empty frame which can have components added to it as needed.
     * Default size is 1000x1000, but this can be overridden.
     * @return Empty JFrame which can hold other components
     */
    public JFrame getMainHomeroomFrame(String title, int height, int width) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.pack();
        if(height == 0) height = 1000;
        if(width == 0) width = 1000;
        frame.setSize(width, height);
        frame.setLayout(null);
        return frame;
    }

    /**
     * Basic method to add buttons to a frame.
     * @param frame Standard {@link Window} that is to be filled.
     * @param buttonTitle Name of the button in the middle.
     * @param buttonHeight How "tall" the button should be.
     * @param buttonWidth How "wide" the button should be.
     * @param locationX The X-coordinate where the button is to be located, relative to the top-left corner of the JFrame.
     * @param locationY The Y-coordinate where the button is to be located, relative to the top-left corner of the JFrame.
     * @return {@link JButton} that the user is able to interact with.
     */
    public JButton addButtonToFrame(Window frame, String buttonTitle, int buttonHeight, int buttonWidth, int locationX, int locationY) {
        JButton button = new JButton(buttonTitle);
        button.setSize(buttonWidth, buttonHeight);
        button.setVisible(true);
        button.setLocation(locationX,locationY);
        frame.add(button);
        return button;
    }

    /**
     * Basic method to add a text field to a frame. This will allow me to add uneditable lines of text to my GUI.
     * @param frame Standard {@link Window} that is to be filled. {@link Window} is being used instead of a {@link JFrame} to allow for more options in terms of what can be done.
     * @param textField The text that is to be displayed within the text field
     * @param locationX The X-coordinate where the label is to be located, relative to the top-left corner of the JFrame.
     * @param locationY The Y-coordinate where the label is to be located, relative to the top-left corner of the JFrame.
     * @return {@link JLabel} that the user is able to see, but cannot do anything with.
     */
    public JLabel addLabelToFrame(Window frame, String textField, int locationX, int locationY, int width, int height) {
        JLabel label = new JLabel(textField);
        frame.add(label);
        label.setBounds(locationX, locationY, width, height);
        label.setVisible(true);
        label.setOpaque(true);
        return label;
    }
}
