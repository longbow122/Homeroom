package me.longbow122.Homeroom.utils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for the core GUI work that needs to be done within Homeroom.
 */
public class GUIUtils {

    /**
     * Will produce an empty frame which can have components added to it as needed.
     * Default size is 1000x1000, but this can be overridden.
     *
     * @return Empty JFrame which can hold other components
     */
    public JFrame openFrame(String title, int height, int width, int locationX, int locationY) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        frame.pack();
        if (height == 0) height = 1000;
        if (width == 0) width = 1000;
        frame.setBounds(locationX, locationY, width, height);
        frame.setLayout(null);
        return frame;
    }

    /**
     * Basic method to add buttons to a frame.
     *
     * @param frame        Standard {@link Window} that is to be filled.
     * @param buttonTitle  Name of the button in the middle.
     * @param buttonHeight How "tall" the button should be.
     * @param buttonWidth  How "wide" the button should be.
     * @param locationX    The X-coordinate where the button is to be located, relative to the top-left corner of the screen.
     * @param locationY    The Y-coordinate where the button is to be located, relative to the top-left corner of the screen.
     * @return {@link JButton} that the user is able to interact with.
     */
    public JButton addButtonToFrame(Window frame, String buttonTitle, int buttonHeight, int buttonWidth, int locationX, int locationY) {
        JButton button = new JButton(buttonTitle);
        frame.add(button);
        button.setSize(buttonWidth, buttonHeight);
        button.setVisible(true);
        button.setLocation(locationX, locationY);
        return button;
    }

    /**
     * Basic method to add a text field to a frame. This will allow me to add uneditable lines of text to my GUI.
     *
     * @param frame     Standard {@link Window} that is to be filled. {@link Window} is being used instead of a {@link JFrame} to allow for more options in terms of what can be done.
     * @param textField The text that is to be displayed within the text field
     * @param locationX The X-coordinate where the label is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the label is to be located, relative to the top-left corner of the screen.
     * @param bold Should the text displayed be emboldened?
     * @param height How "tall" should the text field be?
     * @param width How "wide" should the text field be?
     * @return {@link JLabel} that the user is able to see, but cannot do anything with.
     */
    public JLabel addLabelToFrame(Window frame, String textField, int locationX, int locationY, int width, int height, boolean bold) {
        JLabel label = new JLabel(textField);
        frame.add(label);
        label.setBounds(locationX, locationY, width, height);
        label.setVisible(true);
        label.setOpaque(true);
        if(bold) label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }

    /**
     * Basic method to add a text field for one-lined input. Will come in handy when entering in student information.
     * Is used to generate a text field intended for one-lined input of any size.
     * @param frame Standard {@link Window} that is to be filled.
     * @param locationX The X-coordinate where the fields is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the field is to be located, relative to the top-left corner of the screen.
     * @param width How "wide" the text field should be.
     * @param height How "tall" the text field should be.
     * @return {@link JTextField} that can be further interacted with and used within the code. The user is able to input data here provided a listener has been written.
     */
    public JTextField addTextField(@NotNull Window frame, int locationX, int locationY, int width, int height) {
        JTextField field = new JTextField();
        frame.add(field);
        field.setToolTipText("Enter the username used to log into Homeroom here!");
        field.setBounds(locationX, locationY, width, height);
        field.setVisible(true);
        field.setOpaque(true);
        return field;
    }

    public JPasswordField addPasswordField(Window frame, int locationX, int locationY, int width, int height) {
        JPasswordField field = new JPasswordField();
        frame.add(field);
        field.setToolTipText("Enter the password used to log into Homeroom here!");
        field.setBounds(locationX, locationY, width, height);
        field.setVisible(true);
        field.setOpaque(true);
        return field;
    }
}